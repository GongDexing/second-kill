package cn.net.communion.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import cn.net.communion.constant.ErrCode;
import cn.net.communion.dao.OrderDao;
import cn.net.communion.helper.RedisHelper;
import cn.net.communion.tool.JsonObject;

@Service
public class OrderService {
    private RedisHelper redisHelper;
    private OrderDao orderDao;
    private AtomicInteger requestOrderSum = new AtomicInteger(0);
    private AtomicInteger requserOrderSuccessSum = new AtomicInteger(0);

    @Autowired
    public OrderService(RedisHelper redisHelper, OrderDao orderDao) {
        this.redisHelper = redisHelper;
        this.orderDao = orderDao;

        List<Map<String, Object>> goodsStocks = orderDao.getAllGoodsStock();
        goodsStocks.parallelStream().forEach(goods -> {
            redisHelper.putWithYearExpire("stock:" + goods.get("id"),
                    goods.get("stock").toString());
        });
    }

    public String order(int userId, int goodsId, int number) {
        String stockGoods = "stock:" + goodsId;
        String orderUserGoods = "order:" + userId + ":" + goodsId;
        if (number > 1) {
            return new JsonObject().setErrcode(ErrCode.Single_Number_Exceed).toString();
        }
        String stockTemp = redisHelper.get(stockGoods);
        if (stockTemp == null) {
            return new JsonObject().setErrcode(ErrCode.Wrong_Goods).toString();
        }
        int stock = Integer.parseInt(stockTemp);
        if (stock < 1 || stock < number) {
            return new JsonObject().setErrcode(ErrCode.Stock_Low).toString();
        }
        if ("1".equals(redisHelper.get(orderUserGoods))) {
            return new JsonObject().setErrcode(ErrCode.Order_Goods_Twice).toString();
        }
        redisHelper.putWithYearExpire(stockGoods, "" + (stock - number));
        if (orderDao.orderStock(goodsId) < 1) {
            redisHelper.putWithYearExpire(stockGoods, "0");
            return new JsonObject().setErrcode(ErrCode.Stock_Low).toString();
        }
        redisHelper.putWithYearExpire(orderUserGoods, "1");
        try {
            System.out.println("总共下单数量：" + requestOrderSum.incrementAndGet());
            // System.out.println("begin order: " + userId + "-" + goodsId);
            orderDao.order(userId, goodsId, number);
            // System.out.println("end order: " + userId + "-" + goodsId);
            System.out.println("总共下单成功数量：" + requserOrderSuccessSum.incrementAndGet());
            return new JsonObject().setErrcode(ErrCode.Success).toString();
        } catch (DuplicateKeyException e) {
            return new JsonObject().setErrcode(ErrCode.Order_Goods_Twice).toString();
        } catch (DataIntegrityViolationException e) {
            redisHelper.putWithYearExpire(orderUserGoods, "0");
            redisHelper.putWithYearExpire(stockGoods, "0");
            return new JsonObject().setErrcode(ErrCode.Stock_Low).toString();
        }
    }

}

package cn.net.communion.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void order(int userId, int goodsId, int number) {
        String[] sql = {
                "insert into tbl_order(userId, goodsId, number) values(" + userId + "," + goodsId
                        + "," + number + ")",
                "update tbl_goods set stock=stock-" + number + " where id=" + goodsId + " limit 1"};
        jdbcTemplate.batchUpdate(sql);
    }

    public int orderStock(int goodsId) {
        String stockSql = "select stock from tbl_goods where id=" + goodsId + " limit 1";
        return jdbcTemplate.queryForObject(stockSql, Integer.class);
    }

    public List<Map<String, Object>> getAllGoodsStock() {
        String sql = "select id,stock*2 as stock from tbl_goods";
        return jdbcTemplate.queryForList(sql);
    }
    // static public void main(String[] args) {
    // // int stock = Integer.parseInt("" + (1 - 2));
    // // System.out.println(stock);
    // // if (stock < 1) {
    // // System.out.println("小于0");
    // // }
    // try {
    // System.out.println(1 / 0);
    // } catch (ArithmeticException e) {
    // System.out.println("ArithmeticException");
    // } catch (Exception e) {
    // System.out.println("Exception");
    // }
    // }
}

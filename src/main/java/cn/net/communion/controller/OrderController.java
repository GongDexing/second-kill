package cn.net.communion.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.net.communion.service.OrderService;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    private Random rand = new Random();

    @RequestMapping(path = "/order/{goodsId}")
    public String mastersList(@PathVariable int goodsId, @RequestParam int userId,
            @RequestParam int number) {
        return orderService.order(rand.nextInt(100000), goodsId, number);
    }
}

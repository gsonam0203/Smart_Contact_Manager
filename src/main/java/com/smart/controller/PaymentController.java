package com.smart.controller;

import java.security.Principal;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.*;
import com.smart.dao.OrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.MyOrder;

@Controller
@RequestMapping("/user")
public class PaymentController {
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrderHandler(@RequestBody Map<String , Object> data , Principal principal) throws RazorpayException {
		// debugging 
		System.out.println("created order");
		System.out.println(data);
		
		//fetching amount
		int amount = Integer.parseInt(data.get("amt").toString());
		System.out.println(amount);
		//creating client
		var client = new RazorpayClient("rzp_test_lm6jr3XNPbAvBS" , "ww0l48MKZy8KmNDJivKnqNaG");
		// create order
		JSONObject options = new JSONObject();
		options.put("amount", amount*100); // we take indian ruppee , converting into paise
		options.put("currency", "INR");
		options.put("receipt", "txn_123456");
		
		//create new order
		
		
		Order order = client.Orders.create(options);
		System.out.println(order); // check at razorpay ->payment->order
		
		// save data in database
		MyOrder myOrder = new MyOrder();
		myOrder.setAmount(order.get("amount")+"");
		myOrder.setPaymentId(null);
		myOrder.setStatus("created");
		myOrder.setOrderId(order.get("id"));
		myOrder.setUser(this.userRepo.getUserByUserName(principal.getName()));
		myOrder.setReceipt(order.get("receipt"));
		
		this.orderRepo.save(myOrder); 
		return order.toString();
	}
	
	// to update the data on server
	
	@PostMapping("/update_order")
	@ResponseBody
	public ResponseEntity<?> updateOrderHandler(@RequestBody Map<String , Object> data , Principal principal) {
		MyOrder order = this.orderRepo.findByOrderId(data.get("order_id").toString());
	    order.setPaymentId(data.get("payment_id").toString());
	    order.setStatus(data.get("statuss").toString());
		System.out.println(data);
		 return ResponseEntity.ok(Map.of("msg" , "updated"));
	}

}

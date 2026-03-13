package com.project.EzSplit_Backend.Service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String generateUpiLink(String upiId,String name,Double amount){
        return "upi://pay?pa=" + upiId +
                "&pn=" + name +
                "&am=" + amount +
                "&cu=INR" +
                "&tn=ExpenseSettlement";
    }
}

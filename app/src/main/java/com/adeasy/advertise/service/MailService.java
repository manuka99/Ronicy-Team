package com.adeasy.advertise.service;

import com.adeasy.advertise.model.Order;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface MailService {
    public void SendOrderPlacedEmail(Order order);
    public void destroy();
}

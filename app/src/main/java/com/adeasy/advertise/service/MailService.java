package com.adeasy.advertise.service;

import com.adeasy.advertise.model.Order;

public interface MailService {
    public void SendOrderPlacedEmail(Order order);
    public void destroy();
}

package com.adeasy.advertise.service;

import android.content.Context;
import android.util.Log;

import com.adeasy.advertise.R;
import com.adeasy.advertise.config.Configurations;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.util.EmailUtility;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class MailServiceImpl extends javax.mail.Authenticator implements MailService {

    private static final String TAG = "MailServiceImpl";

    private static final String user = Configurations.EMAIL_RONICY;
    private static final String password = Configurations.PASSWORD_RONICY;
    private static final String mailhost = Configurations.EMAIL_HOST;

    Context context;

    public MailServiceImpl(Context context) {
        this.context = context;
    }

    public void SendOrderPlacedEmail(Order order) {

        InputStream inputStream = context.getResources().openRawResource(R.raw.order_placed_email_template);

        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String str = new String(buffer);
            String ordertype = "";

            str = str.replace("$$orderID$$", order.getId());
            str = str.replace("$$orderPlacedDate$$", order.getPlacedDate().toString());

            if(order.getPayment().getType().equals("COD"))
                ordertype = "Cash On Delivery";
            else
                ordertype = "Credit/Debit Card";

            str = str.replace("$$orderType$$", ordertype);

            str = str.replace("$$deliveryEstimatedDate$$", order.getDeliveryEstimatedDate());

            str = str.replace("$$cus_name$$", order.getCustomer().getName());
            str = str.replace("$$address$$", order.getCustomer().getAddress());
            str = str.replace("$$phone$$", String.valueOf(order.getCustomer().getPhone()));
            str = str.replace("$$email$$", order.getCustomer().getEmail());

            str = str.replace("$$itemImageUrl$$", order.getItem().getImageUrl());
            str = str.replace("$$itemName$$", order.getItem().getItemName());
            str = str.replace("$$itemPrice$$", String.valueOf(order.getItem().getPrice()));

            str = str.replace("$$payment_total$$", String.valueOf(order.getPayment().getAmount()));

            EmailUtility emailUtility = new EmailUtility(order.getCustomer().getEmail(), "Ronicy: Order placed successfully ", str);
            emailUtility.execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Error sending email");
        }

    }

    @Override
    public void destroy() {
        context = null;
    }

}

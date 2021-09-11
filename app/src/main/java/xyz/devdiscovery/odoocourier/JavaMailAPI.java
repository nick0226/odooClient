package xyz.devdiscovery.odoocourier;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import xyz.devdiscovery.odoocourier.ui.sendgeopos.SendGeoPosFragment;

public class JavaMailAPI extends AsyncTask<Void,Void,Void> {

    //Variables
    private Context mContext;
    private Session mSession;

    private String mEmail;
    private String mSubject;
    private String mMessage;

    private ProgressDialog mProgressDialog;

    //Constructor
    public JavaMailAPI(Context mContext, String mEmail, String mSubject, String mMessage) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Show progress dialog while sending email
        mProgressDialog = ProgressDialog.show(mContext,"Отправка сообщения", "Пожалуйста подождите...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismiss progress dialog when message successfully send
        mProgressDialog.dismiss();

        //Show success toast
        Toast.makeText(mContext,"Сообщение отправлено",Toast.LENGTH_SHORT).show();
    }



    @Override
    protected Void doInBackground(Void... params) {

        Properties props = new Properties();

        // Proporties for smtp pro100systems
        props.put("mail.smtp.host", "pro100systems.com.ua");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth.mechanisms", "CRAM-MD5");
        props.put("mail.smtp.sasl.enable", "true");

        //Creating a new session
        mSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
                    }
                });


        try {

            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(mSession);
            //Setting sender address
            //mm.setFrom(new InternetAddress(Utils.MyEmail));
            mm.setFrom(new InternetAddress(SendGeoPosFragment.MY_EMAIL));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            // Добавление темы сообщения
            mm.setSubject(mSubject);
            // Добавление сообщения в теле
            mm.setText(mMessage);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mMessage);


            //Отправка сообщениея
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}

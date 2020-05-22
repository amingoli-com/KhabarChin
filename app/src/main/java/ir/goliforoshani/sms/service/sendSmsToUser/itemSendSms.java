package ir.goliforoshani.sms.service.sendSmsToUser;

public class itemSendSms {
    String number,massage,time;

    public itemSendSms(String number, String massage, String time) {
        this.number = number;
        this.massage = massage;
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public String getMassage() {
        return massage;
    }

    public String getTime() {
        return time;
    }
}

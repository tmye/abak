package tg.tmye.kaba.data.customer;

/**
 * By abiguime on 06/01/2018.
 * email: 2597434002@qq.com
 */

public class Customer {

    public int id;
    public String first_name;
    public String last_name;
    public String nickname;
    public String phone_number;
    public String birthday;
    public int gender; /* 1 female - 2 male - 3 dontwannasay - 0 notsetyet */
    public String ppicture;
    public boolean is_gender_to_set;

    public static Customer fakeCustomer() {
        Customer customer = new Customer();
        customer.first_name = "Esso";
        customer.last_name = "Modeste";
        customer.phone_number = "99319942";
        customer.nickname = "badboy92";
        customer.birthday = "21-01-1992";
        customer.gender = 1;
        customer.ppicture = "/sample/advert_full/p1.jpg";
        customer.is_gender_to_set = false;

        return customer;
    }
}

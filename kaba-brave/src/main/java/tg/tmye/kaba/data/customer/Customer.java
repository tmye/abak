package tg.tmye.kaba.data.customer;

/**
 * By abiguime on 06/01/2018.
 * email: 2597434002@qq.com
 */

public class Customer {

    public int id;
    public String username;
    public String phone_number;
    public String nickname;
    public String birthday;
    public int gender; /* 1 female - 2 male - 3 dontwannasay - 0 notsetyet */
    public String profile_picture;
    public String theme_picture;
    public int is_gender_to_set;

    public static Customer fakeCustomer() {
        Customer customer = new Customer();
        customer.phone_number = "99319942";
        customer.username = "Esso Modeste";
        customer.birthday = "21-01-1992";
        customer.gender = 1;
        customer.profile_picture = "/sample/advert_full/p1.jpg";
        customer.is_gender_to_set = 0; // 0false, 1true

        return customer;
    }
}

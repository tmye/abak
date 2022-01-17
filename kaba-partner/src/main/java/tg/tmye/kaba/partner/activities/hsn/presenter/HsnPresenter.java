package tg.tmye.kaba.partner.activities.hsn.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.partner.ILog;
import tg.tmye.kaba.partner._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.partner.activities.commands.contract.CommandDetailsContract;
import tg.tmye.kaba.partner.activities.hsn.contract.HSNContract;
import tg.tmye.kaba.partner.data.command.Command;
import tg.tmye.kaba.partner.data.command.source.CommandRepository;
import tg.tmye.kaba.partner.data.district.DeliveryDistrict;

/**
 * By abiguime on 2021/9/15.
 * email: 2597434002@qq.com
 */
public class HsnPresenter implements HSNContract.Presenter {


    private final HSNContract.View view;

    private final CommandRepository commandRepository;
    private Gson gson = new Gson();

    public HsnPresenter(CommandRepository commandRepository, HSNContract.View view) {
        this.view = view;
        this.commandRepository = commandRepository;
    }

    @Override
    public void loadDeliveryDistricts() {
        view.showLoading(true);
        commandRepository.loadDeliveryDistricts(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);
                // work on the data, and get it to the view
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    // get daily restaurants
                    DeliveryDistrict[] deliveryDistricts_ =
                            gson.fromJson(obj.get("data").getAsJsonArray(), new TypeToken<DeliveryDistrict[]>() {
                            }.getType());
                    List<DeliveryDistrict> deliveryDistricts = Arrays.asList(deliveryDistricts_);
                    view.inflateDeliveryDistricts(deliveryDistricts);
                } catch (Exception e){
                    e.printStackTrace();
                    view.inflateDeliveryDistrictsError();
                }
            }
        });
    }

    @Override
    public void computeDeliveryBilling(DeliveryDistrict deliveryDistrict) {
        view.showLoading(true);
        commandRepository.computeDeliveryBilling(deliveryDistrict, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);
                // work on the data, and get it to the view
//                        yesOrNoWithResponse.yes(Command.fakeList(6));
                /* inflate the result into a list of things */
                try {
                    ILog.print(jsonResponse);
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    if (error == 1){
                        view.outOfRange();
                    } else {
                        JsonObject data = obj.get("data").getAsJsonObject();
                        String price = data.get("price").toString();
                        String km = data.get("km").toString();
                        // get daily restaurants
                        view.inflateDeliveryPrice(deliveryDistrict, price);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    view.inflateDeliveryPriceError();
                }
            }
        });
    }

    @Override
    public void createHSN(String district_id, String phone_number, String food_price, String more_informations) {
        view.showLoading(true);
        commandRepository.createHSN(district_id, phone_number, food_price, more_informations, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);
                // work on the data, and get it to the view
//                        yesOrNoWithResponse.yes(Command.fakeList(6));
                /* inflate the result into a list of things */
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    // get daily restaurants
                       view.hsnCreateState(error == 0);
                } catch (Exception e){
                    e.printStackTrace();
                    view.hsnCreateState(false);
//                    view.inflateDeliveryPriceError();
                }
            }
        });
    }

    @Override
    public void start() {

    }
}

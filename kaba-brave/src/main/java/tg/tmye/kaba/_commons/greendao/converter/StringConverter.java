package tg.tmye.kaba._commons.greendao.converter;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * By abiguime on 19/12/2017.
 * email: 2597434002@qq.com
 */

public class StringConverter implements PropertyConverter<List<String>, String> {

    @Override
    public List<String> convertToEntityProperty(String databaseValue) {

      return Arrays.asList(databaseValue.split("#"));
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {

        String res = "";
        for (int i = 0; i < entityProperty.size(); i++) {
            res+= (entityProperty.get(i)+"#");
        }
        res = res.substring(0, res.length()-1);
        return res;
    }
}

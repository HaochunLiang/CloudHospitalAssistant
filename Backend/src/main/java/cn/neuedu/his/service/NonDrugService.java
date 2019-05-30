package cn.neuedu.his.service;
import cn.neuedu.his.model.NonDrug;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface NonDrugService extends Service<NonDrug> {

    NonDrug selectNonDrugByName(String name);

    NonDrug selectNonDrugByCode(String code);
    List<NonDrug> findByName (String name);
    JSONObject getAll ();
}

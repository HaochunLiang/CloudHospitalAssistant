package cn.neuedu.his.controller;

import cn.neuedu.his.model.*;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.MedicalRecordService;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    MedicalRecordService medicalRecordService;

    //PARTONE-门诊病例首页

    /**
     *通过患者id获得该患者所有的病历
     * @param patientID
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    @GetMapping("/getAllRecord/{patientID}")
    public JSONObject getAllRecordByPatientId(@PathVariable("patientID") Integer patientID, Authentication authentication)
    throws AuthenticationServiceException {
        Integer doctorID;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<MedicalRecord> list=medicalRecordService.getAllByPatientId(patientID);
        if (list==null){
            list=new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    /**
     * 通过患者名字找到所有挂在该医生的待诊挂号
     * use patient name to registration
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping("/getByName/{name}")
    public JSONObject getRegistrationByPatientName(@PathVariable("name") String name, Authentication authentication ) {
        Integer doctorID;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<Registration> list=registrationService.getRegistrationByPatientName(name,doctorID,Constants.WAITING_FOR_TREATMENT);
        if (list==null){
            list=new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    /**
     * 找到该医生对应的所有待诊挂号
     * @param authentication
     * @return
     */
    @GetMapping("/getAllWait")
    public JSONObject getAllWaitingRegistration(Authentication authentication){
        Integer doctorID;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<Registration> list=registrationService.getAllWaitingRegistration(doctorID,Constants.WAITING_FOR_TREATMENT);
        if (list==null){
            list=new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }


    /**
     * 门诊医生查看全院病例模板
     * @param authentication
     * @return
     */
    @GetMapping("/getHospitalMR")
    public JSONObject getHospitalMR(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  CommonUtil.successJson(doctorService.getHospitalMR(doctorID,Constants.HOSPITALLEVEL));
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看所在科室病例模板
     * @param authentication
     * @return
     */
    @GetMapping("/getDeptMR")
    public JSONObject getDeptMR(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  CommonUtil.successJson(doctorService.getDeptMR(doctorID,Constants.DEPTLEVEL));
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看个人病例模板
     * @param authentication
     * @return
     */
    @GetMapping("/getPersonalMR")
    public JSONObject getPersonalMR(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return CommonUtil.successJson( doctorService.getPersonalMR(doctorID,Constants.PERSONALLEVEL));
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 通过部分连续的字段获得所有疾病
     * @param name
     * @param authentication
     */
    @GetMapping("/findDisease/{name}")
    public JSONObject findDiseaseByName(@PathVariable("name") String name,Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  CommonUtil.successJson(doctorService.findDiseaseByName(name));
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    @GetMapping("/getAllDiseases")
    public JSONObject getAllDiseases(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  CommonUtil.successJson(doctorService.getAllDiease());
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 通过部分连续的字段获得所有非药
     * @param name
     * @param authentication
     */
    @GetMapping("/findNonDrug/{name}")
    public JSONObject findNonDrugByName(@PathVariable("name") String name,Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            List<NonDrug> nonDrugs = doctorService.findNonDrugByName(name);
            return CommonUtil.successJson(nonDrugs);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 获得所有非药物品
     * @param authentication
     * @return
     */
    @GetMapping("/getAllNonDrug")
    public JSONObject getAllNonDrug(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  CommonUtil.successJson(doctorService.getAllNonDrug());
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }


    /**
     * 医生初诊提交，更新该挂号状态
     * update the registration state as first diagnose which is 803
     * @param object
     * @return
     */
    @PostMapping("/firstDiagnose")
    public JSONObject setFirstDiagnose(@RequestBody JSONObject object, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Integer registrationID=null;
        try{
            registrationID=Integer.parseInt(object.get("registrationID").toString());
            if (registrationID==null)
                throw new NumberFormatException();
        }catch (NumberFormatException n){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        }
        MedicalRecord medicalRecord = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        medicalRecord.setRegistrationId(registrationID);
        Diagnose diagnose=JSONObject.parseObject(object.get("diagnose").toString(),Diagnose.class);
        if (diagnose.getDiseaseId()==null)
            return  CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("diseaseId"));
        JSONObject object1;
        try {
            object1=doctorService.setFirstDiagnose(registrationID, medicalRecord,diagnose);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("medicalRecord"));
        }
        return object1;
    }

    /**
     * 存为全院病历模板
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveHospitalMRTemplate")
    public JSONObject saveHospitalMRTemplate(@RequestBody JSONObject object,Authentication authentication) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer doctorID=null;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            isChiefDoctor(doctorID);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        String name=(String) object.get("name");
        if (name==null || name.equals("") )
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));
        MedicalRecord record=JSONObject.parseObject(object.get("medicalRecord").toString(),MedicalRecord.class);
        return CommonUtil.successJson( doctorService.saveMRTemplate(record, doctorID, name,Constants.HOSPITALLEVEL));
    }

    /**
     * 存为科室病历模板
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveDeptMRTemplate")
    public JSONObject saveDeptMRTemplate(@RequestBody JSONObject object,Authentication authentication) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer doctorID=null;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            aboveDeputyChiefDoctor(doctorID);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        String name=(String) object.get("name");
        if (name==null || name.equals("") )
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));
        MedicalRecord record=JSONObject.parseObject(object.get("medicalRecord").toString(),MedicalRecord.class);
        return CommonUtil.successJson( doctorService.saveMRTemplate(record, doctorID, name,Constants.DEPTLEVEL));
    }

    /**
     * 存为个人病历模板
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/savePersonalMRTemplate")
    public JSONObject savePersonalMRTemplate(@RequestBody JSONObject object,Authentication authentication) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer doctorID=null;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            aboveATTENDING_DOCTOR(doctorID);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        String name=(String) object.get("name");
        if (name==null || name.equals("") )
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));
        MedicalRecord record=JSONObject.parseObject(object.get("medicalRecord").toString(),MedicalRecord.class);
        return CommonUtil.successJson( doctorService.saveMRTemplate(record, doctorID, name,Constants.PERSONALLEVEL));
    }


    /**
     * 主任医师权限检验
     * @param  id 医生
     * @return
     * @throws AuthenticationServiceException
     */
    @Transactional
    public boolean  isChiefDoctor(Integer id) throws AuthenticationServiceException{
        Doctor doctor=doctorService.findById(id);
        if (doctor.getTitleId().equals(Constants.CHIEF_DOCTOR)) {
            return true;
        } else {
            throw new AuthenticationServiceException("ChiefDoctor");
        }
    }


    /**
     * 副主任医师以及以上权限检验
     * @param  id 医生
     * @return
     * @throws AuthenticationServiceException
     */
    @Transactional
    public boolean aboveDeputyChiefDoctor(Integer id) throws AuthenticationServiceException{
        Doctor doctor=doctorService.findById(id);
        if (doctor.getTitleId().equals(Constants.DEPUTY_CHIEF_DOCTOR) ||  doctor.getTitleId().equals(Constants.CHIEF_DOCTOR)) {
            return true;
        } else {
            throw new AuthenticationServiceException("DeputyChiefDocto");
        }
    }


    /**
     * 主治医师以及以上权限检验
     * @param  id 医生
     * @return
     * @throws AuthenticationServiceException
     */
    @Transactional
    public boolean  aboveATTENDING_DOCTOR(Integer id) throws AuthenticationServiceException{
        Doctor doctor=doctorService.findById(id);
        if (doctor.getTitleId().equals(Constants.DEPUTY_CHIEF_DOCTOR) ||  doctor.getTitleId().equals(Constants.CHIEF_DOCTOR) || doctor.getTitleId().equals(Constants.ATTENDING_DOCTOR)) {
            return true;
        } else {
            throw new AuthenticationServiceException("ATTENDING_DOCTOR");
        }
    }






    //PARTTWO-检查/检验/申请
    @GetMapping("/openInspection")
    public JSONObject openInspection(JSONObject  object ,Authentication authentication){
        Integer doctorID=null;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
       try {
           Integer registrationId=Integer.parseInt(object.get("registrationId").toString());
           return doctorService.openInspection(registrationId);
       }catch (Exception e){
           return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
       }
    }


    /**
     * 门诊医生查看全院检查模板
     * @param authentication
     * @return
     */
    @GetMapping("/getHospitalCheckTemps")
    public JSONObject getHospitalCheckTemps(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  CommonUtil.successJson(doctorService.getHospitalCheckTemps(doctorID,Constants.HOSPITALLEVEL));
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看所在科室检查模板
     * @param authentication
     * @return
     */
    @GetMapping("/getDeptCheckTemps")
    public JSONObject getDeptCheckTemps(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  CommonUtil.successJson(doctorService.getDeptCheckTemps(doctorID,Constants.DEPTLEVEL));
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看个人检查模板
     * @param authentication
     * @return
     */
    @GetMapping("/getPersonalCheckTemps")
    public JSONObject getPersonalInspectionTemps(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return CommonUtil.successJson( doctorService.getPersonalCheckTemps(doctorID,Constants.PERSONALLEVEL));
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }



    /**
     * 保存医生申请的非药项目
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveInspection")
    public JSONObject saveInspection(@RequestBody  JSONObject object,Authentication authentication){
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        try{
            return doctorService.saveInspections(object);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
    }


    /**
     * 保存检查模板
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveInspectionTem")
    public JSONObject saveInspectionTem(@RequestBody JSONObject object,Authentication authentication){
        Integer doctorId;
        try {
            doctorId=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Integer level;
        try {
            level=Integer.parseInt(object.get("level").toString());
            if(level.equals(Constants.HOSPITALLEVEL)){
                isChiefDoctor(doctorId);
            }else  if(level.equals(Constants.DEPTLEVEL)){
                aboveDeputyChiefDoctor(doctorId);
            }else if (level.equals(Constants.PERSONALLEVEL)){
                aboveATTENDING_DOCTOR(doctorId);
            }else {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("noSuchLevel"));
            }
        }catch (AuthenticationServiceException ex){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(ex.getMessage()));
        } catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("level"));
        }
        Boolean isNew=(Boolean) object.get("isNew");
        if(isNew){
            try {
                return doctorService.saveInspectionTemplate(object, level, doctorId);
            }catch (Exception e){
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
            }
        }else {
            try{
                return doctorService.saveInspectionAsTemplate(object, level, doctorId);
            }catch (Exception e){
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
            }
        }
    }




    /**
     * update the registration state as suspect diagnose which is 804
     * 当医生申请检查，应该更新该挂号状态为疑诊
     * @param id
     * @return
     */
    @Transactional
    public JSONObject updateStateToSuspectDiagnose(@RequestBody Integer id, Authentication authentication) {
             try {
                 PermissionCheck.isOutpatientDoctor(authentication);
             }catch (AuthenticationServiceException a){
                 return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
             }
        Registration registration = registrationService.findById(id);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.SUSPECT);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }

    /**
     * update the registration state as suspect diagnose which is 804
     * 当医生点击了确诊的时候，应该更新该挂号状态为确诊
     * @param id
     * @return
     */
    @Transactional
    public JSONObject updateStateToFinalDiagnose(@RequestBody Integer id, Authentication authentication)  {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Registration registration = registrationService.findById(id);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.FINAL_DIAGNOSIS);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }

    /**
     * update the registration state as suspect diagnose which is 804
     * 当医生提交处方，应该更新该挂号状态为诊毕
     * @param id
     * @return
     */
    @Transactional
    public JSONObject updateStateToFinishDiagnose(@RequestBody Integer id, Authentication authentication){
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Registration registration = registrationService.findById(id);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.FINISH_DIAGNOSIS);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }





}

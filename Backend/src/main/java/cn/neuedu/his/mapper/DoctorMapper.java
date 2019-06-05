package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public interface DoctorMapper extends MyMapper<Doctor> {
    public Integer getDeptNo(Integer id);
    ArrayList<Doctor> getAllNotDelete();
}
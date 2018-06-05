package blog.service.impl;

import blog.dao.AdminLoginLogDao;
import blog.domain.AdminLoginLog;
import blog.service.AdminLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminLoginLogServiceImpl implements AdminLoginLogService{

    @Autowired
    public AdminLoginLogDao adminLoginLogDao;
    public List<AdminLoginLog> selectRencent(Integer adminId) {
        return adminLoginLogDao.selectRencent(adminId);
    }

    public int insert(AdminLoginLog adminLoginLog) {
        return adminLoginLogDao.insert(adminLoginLog);
    }

    public int selectCountByAdminId(int adminId) {
        return adminLoginLogDao.selectCountByAdminId(adminId);
    }
}

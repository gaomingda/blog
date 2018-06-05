package blog.service.impl;

import blog.dao.AdminDao;
import blog.domain.Admin;
import blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    public AdminDao adminDao;
    public Admin getById(Integer id) {
        return adminDao.selectByPrimaryKey(id);
    }
}

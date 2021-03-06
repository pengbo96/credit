package com.tjl.credit.service;

import com.tjl.credit.dao.*;
import com.tjl.credit.domain.*;
import com.tjl.credit.utils.FileUtils;
import com.tjl.credit.utils.RetResponse;
import com.tjl.credit.utils.RetResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.util.List;

/**
 * @Author PengBo
 * @CreateTime 2019/5/7 22:54
 * @Version 1.0.0
 */
@Service
public class AdminService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private CollegeMapper collegeMapper;
    @Resource
    private ProfessionalMapper professionalMapper;
    @Resource
    private TclassMapper tclassMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private CreditMapper creditMapper;
    @Resource
    private CreditAndCheckMapper creditAndCheckMapper;

    public RetResult queryUserByNumber(User user) throws Exception {
        int flag = userMapper.queryUserByNumber(user);
        if (flag == 1) {
            return RetResponse.makeOKRsp("学号已经存在");
        } else if (flag == 0) {
            return RetResponse.makeOKRsp("学号可用");
        }
        return RetResponse.makeErrRsp("出现错误");
    }

    public RetResult createUser(User user) throws Exception {
        int flag = userMapper.insertSelective(user);
        if (flag == 1) {
            return RetResponse.makeOKRsp("添加成功");
        }
        return RetResponse.makeErrRsp("添加失败");
    }

    public RetResult queryAllUser() throws Exception {

        List<User> userList = userMapper.queryAllUser();
        if (userList.size() >= 0) {
            return RetResponse.makeOKRsp("查询成功", userList);
        } else {
            return RetResponse.makeErrRsp("查询失败");
        }
    }

    public RetResult createRole(Role role) throws Exception {

        int flag = roleMapper.createRole(role);
        if (flag > 0) {
            return RetResponse.makeOKRsp("权限修改成功");
        } else if (flag == 0) {
            return RetResponse.makeOKRsp("权限没有任何修改");
        }
        return RetResponse.makeErrRsp("权限修改失败");
    }

    public RetResult insertRole(Role role) throws Exception {
        int flag = roleMapper.insertRole(role);
        if (flag == 1) {
            return RetResponse.makeOKRsp("角色插入成功");
        } else {
            return RetResponse.makeErrRsp("角色插入失败");

        }
    }

    public RetResult queryAllRole() throws Exception {
        List<Role> roleList = roleMapper.queryAllRole();
        if (roleList.size() > 0) {
            return RetResponse.makeOKRsp("查询成功", roleList);
        } else {
            return RetResponse.makeErrRsp("查询失败");
        }
    }

    public RetResult deleteRole(Role role) throws Exception {
        int flag = roleMapper.deleteRole(role);
        if (flag == 1) {
            return RetResponse.makeOKRsp("删除成功");
        } else {
            return RetResponse.makeErrRsp("删除失败");

        }
    }

    public RetResult createCollege(College college) {
        int flag = collegeMapper.insert(college);
        if (flag == 1) {
            return RetResponse.makeOKRsp("添加成功");
        } else {
            return RetResponse.makeErrRsp("添加失败");
        }
    }

    public RetResult createProfessional(Professional professional) {
        int flag = professionalMapper.insert(professional);
        if (flag == 1) {
            return RetResponse.makeOKRsp("添加成功");
        } else {
            return RetResponse.makeErrRsp("添加失败");
        }
    }

    public RetResult createTclass(Tclass tclass) {
        int flag = tclassMapper.insert(tclass);
        if (flag == 1) {
            return RetResponse.makeOKRsp("添加成功");
        } else {
            return RetResponse.makeErrRsp("添加失败");
        }
    }

    public RetResult updateUser(User user) throws Exception {
        int flag = userMapper.updateUser(user);
        if (flag == 1) {
            return RetResponse.makeOKRsp("修改成功");
        } else if (flag == 0) {
            return RetResponse.makeOKRsp("没有任何修改");
        } else {
            return RetResponse.makeErrRsp("修改失败");
        }
    }

    public RetResult deleteUser(User user) throws Exception {
        int flag = userMapper.deleteUser(user);
        if (flag == 1) {
            return RetResponse.makeOKRsp("删除成功");
        } else {
            return RetResponse.makeErrRsp("删除失败");
        }
    }

    public RetResult uploadFile(MultipartFile file, String title, String content, String userNumber) throws Exception {
        String fileName = FileUtils.getFileName(file);
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setFile(fileName);
        notice.setDate(new Date(System.currentTimeMillis()));
        int flag = noticeMapper.insert(notice);

        String url = FileUtils.makeDir(userNumber);

        if (FileUtils.uploadFile(file, fileName, url) && flag == 1) {
            return RetResponse.makeOKRsp("公告发布成功");
        } else {
            return RetResponse.makeErrRsp("公告发布失败");
        }
    }

    public RetResult lookNotice() throws Exception {
        List<Notice> noticeList = noticeMapper.lookNotice();
        if (noticeList.size() > 0) {
            return RetResponse.makeOKRsp("查询成功", noticeList);
        } else if (noticeList.size() == 0) {
            return RetResponse.makeOKRsp("没有数据");
        }
        return RetResponse.makeErrRsp("查询失败");
    }

    public void lookNoticeFile(String fileName, String userNumber, HttpServletResponse response) throws Exception {
        String url = FileUtils.makeDir(userNumber);
        File file = new File(url + "\\" + fileName);
        FileUtils.downloadFile(fileName, response, file);
    }

    public RetResult queryNoticeById(Integer id) throws Exception {
        Notice notice = noticeMapper.queryById(id);
        if (notice == null) {
            return RetResponse.makeErrRsp("查询失败");
        }
        return RetResponse.makeOKRsp("查询成功", notice);
    }

    public RetResult queryAllCollege() throws Exception {
        List<College> collegeList = collegeMapper.queryAllCollege();
        if (collegeList.size() > 0) {
            return RetResponse.makeOKRsp("查询成功", collegeList);
        } else {
            return RetResponse.makeErrRsp("查询失败");
        }
    }

    public RetResult queryProfessional(Professional professional) throws Exception {
        List<Professional> professionalList = professionalMapper.queryProfessional(professional);
        if (professionalList.size() > 0) {
            return RetResponse.makeOKRsp("查询成功", professionalList);
        } else {
            return RetResponse.makeErrRsp("查询失败");
        }
    }

    public RetResult queryTclass(Tclass tclass) throws Exception {
        List<Tclass> tclassList = tclassMapper.queryTclass(tclass);
        if (tclassList.size() > 0) {
            return RetResponse.makeOKRsp("查询成功", tclassList);
        } else {
            return RetResponse.makeErrRsp("查询失败");
        }
    }

    public RetResult deleteCollege(College college) throws Exception {
        Professional professional = new Professional();
        professional.setCollegeId(college.getId());
        List<Professional> professionalList = professionalMapper.queryProfessional(professional);
        if (professionalList.size() > 0) {
            return RetResponse.makeOKRsp("学院下有相关专业");
        } else {
            collegeMapper.deleteCollege(college);
            return RetResponse.makeOKRsp("删除成功");
        }
    }

    public RetResult deleteProfessional(Professional professional) throws Exception {
        Tclass tclass = new Tclass();
        tclass.setProfessionalId(professional.getId());
        List<Tclass> tclassList = tclassMapper.queryTclass(tclass);
        if (tclassList.size() > 0) {
            return RetResponse.makeOKRsp("专业下有相关班级");
        } else {
            professionalMapper.deleteProfessional(professional);
            return RetResponse.makeOKRsp("删除成功");
        }
    }

    public RetResult queryUser(Tclass tclass) throws Exception {
        Tclass tclass1 = tclassMapper.queryOnlyTclass(tclass);
        User user = new User();
        user.setTclass(tclass1.getTclass());
        System.out.println(user.getTclass());
        List<User> userList = userMapper.queryUser(user);
        if (userList.size() == 0) {
            return RetResponse.makeOKRsp("没有学生");
        }
        return RetResponse.makeOKRsp("查询成功", userList);
    }

    public RetResult deleteTclass(Tclass tclass) throws Exception {
        Tclass tclass1 = tclassMapper.queryOnlyTclass(tclass);
        User user = new User();
        user.setTclass(tclass1.getTclass());
        System.out.println(user.getTclass());
        List<User> userList = userMapper.queryUser(user);
        if (userList.size() > 0) {
            return RetResponse.makeOKRsp("改班级下有学生");
        } else {
            tclassMapper.deleteTclass(tclass);
            return RetResponse.makeOKRsp("删除成功");
        }

    }

    public RetResult queryCreditState(CreditAndCheck creditAndCheck) throws Exception {
        List<Credit> creditList = creditAndCheckMapper.queryCreditState(creditAndCheck);
        if (creditList.size()>0){
            return RetResponse.makeOKRsp("查询成功",creditList);
        }else {
            return RetResponse.makeOKRsp("没有记录");

        }
    }

    public RetResult queryCollegeCredit()throws Exception {
        return RetResponse.makeOKRsp("没有记录");
    }
}

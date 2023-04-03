package com.tian.servlet;

import com.tian.config.SpringConfig;
import com.tian.domain.Atm;
import com.tian.domain.User;
import com.tian.service.UserService;
import com.tian.service.serviceImpl.AtmServiceImpl;
import com.tian.service.serviceImpl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

@WebServlet({"/user/login", "/user/editBalance"})
public class UserServlet extends HttpServlet {

    static AnnotationConfigApplicationContext ctx = null;

    static {
        ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/user/login")) {
            // 执行登录操作。
            System.out.println("执行登录操作。");
            doLogin(request, response);
        } else if (request.getServletPath().equals("/user/add")) {
            System.out.println("添加用户操作。");
            doAddUser(request, response);
        } else if (request.getServletPath().equals("/user/delUser")) {
            System.out.println("删除用户操作。");
            doDelUser(request, response);
        } else if (request.getServletPath().equals("/user/editUser")) {
            System.out.println("执行修改用户操作。");
            doEditUser(request, response);
        } else if (request.getServletPath().equals("/user/selectUser")) {
            System.out.println("执行查找用户功能。");
            doSelectUser(request, response);
        } else if (request.getServletPath().equals("/user/editBalance")) {
            System.out.println("执行修改余额功能。");
            // 先判断有没有用户登录。
            if (request.getSession().getAttribute("username") == null) {
                // 没有用用户登录。
                // 重定向到登录页面。
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
            doEditBalance(request, response);
        }
    }

    /**
     * 修改用户余额功能。
     *
     * @param request  请求
     * @param response 相应
     */
    private void doEditBalance(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从前端获取数据。
        String userid = request.getParameter("userid"); // 用户id。
        String atmid = request.getParameter("atmid");
        int changedBalance = Integer.parseInt(request.getParameter("changedBalance")); // 资金该变量。
        String direction = request.getParameter("direction"); // 资金流动方向，in表示入账，out表示出账。


        // 获取用户服务层对现象。
        UserServiceImpl userServiceImpl = ctx.getBean(UserServiceImpl.class);
        AtmServiceImpl atmServiceImpl = ctx.getBean(AtmServiceImpl.class);

        // 获取用户对象信息。
        User userInfo = userServiceImpl.selectUserById(Integer.parseInt(userid));
        // 获取atm信息。
        Atm atmInfo = atmServiceImpl.selectAtmInfo(Integer.parseInt(atmid));

        // 选择出入账。
        if ("in".equals(direction)) {
            // 入账。
            // 入账没有要检查的内容，可以直接入账。
            userServiceImpl.addBalance(Integer.parseInt(userid), changedBalance); // 为账户添加余额。
        } else if ("out".equals(direction)) {
            // 出账。

            // 出账的时候。要依次检查账户余额和atm机余额是否够取钱。
            if (userInfo.getBalance() >= changedBalance) {
                // 账户余额大于修改余额。

                // 判断atm余额是否大于修改余额。
                if (atmInfo.getAtmBalance() >= changedBalance) {
                    // atm余额大于改变余额。
                    // 为用户扣款。
                    userServiceImpl.reduceBalance(Integer.parseInt(userid), changedBalance);
                    // 为atm扣款。
                    atmServiceImpl.reduceBalance(Integer.parseInt(atmid), changedBalance);
                    // 重定向到首页。
                    request.getRequestDispatcher(request.getContextPath() + "/function/panel").forward(request, response);
                } else {
                    // atm余额不足。
                    request.getRequestDispatcher(request.getContextPath() + "/pages/atmNotEnough.jsp").forward(request, response);
                }
            } else {
                // 账户余额小于修改余额，重定向到登录失败页面。
                request.getRequestDispatcher(request.getContextPath() + "/pages/accountNotEnough.jsp").forward(request, response);
            }
        }
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("测试信息，执行登录函数。");
        // 获取用户信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 获取使用的atm机的id，并且保存到session对象中。
        String atmid = request.getParameter("atmid");
        request.getSession().setAttribute("atmid", atmid);
        // 获取服务层实现类。
        UserService userServiceImpl = ctx.getBean(UserService.class);
        // 判断登录成功。
        if (userServiceImpl.ifLogIn(username, password)) {
            // 判断是否有被拒绝登录。
            if (request.getSession().getAttribute("failedCount") != null && (int) request.getSession().getAttribute("failedCount")>=5) {
                // 获取到登录失败次数记录。
                // 失败次数超过五次。
                // 拒绝登录。
                response.sendRedirect(request.getContextPath() + "/pages/rejectedPage.jsp");
            }
            // 登录成功。
            // 获取用户信息，并且保存到session中。
            // 通过service对象获取user的id
            int userId = userServiceImpl.getUserIdByNameAndPwd(username, password);
            // 在session中保存用户的登录信息。
            System.out.println("将userid="+userId+"保存到session中。");
            request.getSession().setAttribute("userid", userId);
            // request.getSession().setAttribute("atmid", atmid); // 前面已经封装过了。
            // 删除session的登录失败计数器。
            if (request.getSession().getAttribute("failedCount") != null) {
                // 检测到session中包含有登录失败计数器。
                // 删除登录失败计数器。
                request.getSession().removeAttribute("failedCount");
            }
            System.out.println("userServlet程序运行到转发步骤。");
            request.getRequestDispatcher("/function/panel").forward(request, response);
            // 总结：如果登录成功，现在session中应该有两个键值对。
            // 一个是表示正在使用的atm机器的id：atmid，另一个是表示已经登陆的用户的用户id。
            // 如果有登录失败计数器，会在登录成功的时候删除掉。
        } else {
            // 登录失败操作。
            // 先从session获取登录失败计数值。
            HttpSession session = request.getSession();
            // 操作failedCount值
            if (session.getAttribute("failedCount") != null) {
                // 出错次数对象非空。
                // 说明前面有失败过，直接添加新数字。
                int failedCount = (int) session.getAttribute("failedCount");
                session.setAttribute("failedCount", failedCount + 1);
            } else {
                // 对象为空，说明没有获取到对象。
                // 没有对象就在session中添加一个新的对象。
                session.setAttribute("failedCount", 1);
            }
            // 判断是否超过最大失败次数。
            int failedCount = (int) session.getAttribute("failedCount");
            if (failedCount >= 3) {
                // 错误次数达到五次，跳转到拒绝登录页面。
                response.sendRedirect(request.getContextPath() + "/pages/rejectedPage.jsp");
            } else {
                // 登录失败，跳转到登录失败jsp
                response.sendRedirect(request.getContextPath() + "/pages/failedPage.jsp");
            }
        }
    }

    // 下面的函数都不需要了。////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void doSelectUser(HttpServletRequest request, HttpServletResponse response) {
        // 暂无。
    }


    private void doEditUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取到用户信息。
        User userInfo = (User) request.getAttribute("userInfo");
        // 执行删除用户操作。
        UserService userServiceImpl = ctx.getBean(UserService.class);
        userServiceImpl.editUser(userInfo);
        System.out.println("来自UserServlet.doEditUser，出现这行话说明编辑用户信息完毕。");
        // 页面跳转
        request.getRequestDispatcher(request.getContextPath() + "/function/panel").forward(request, response);
    }

    private void doDelUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取并转化userId
        int userId = Integer.parseInt(request.getParameter("userId"));
        // 执行删除用户操作。
        UserService userServiceImpl = ctx.getBean(UserService.class);
        boolean delOk = userServiceImpl.delUser(userId);
        System.out.println("来自函数UserService.doAddUser，删除结果为：" + delOk);
        // 页面跳转
        request.getRequestDispatcher(request.getContextPath() + "/function/panel").forward(request, response);
    }

    /**
     * 添加用户操作
     *
     * @param request  请求
     * @param response 相应
     */
    private void doAddUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取到用户信息。
        User userInfo = (User) request.getAttribute("userInfo");
        // 添加用户。
        UserService userServiceImpl = ctx.getBean(UserService.class);
        // 执行操作。
        boolean addOk = userServiceImpl.addUser(userInfo);
        // 控制台输出结果。
        System.out.println("来自函数UserService.doAddUser，添加结果为：" + addOk);
        // 页面跳转。
        request.getRequestDispatcher(request.getContextPath() + "/function/panel").forward(request, response);
    }

    /**
     * 登录操作。
     * @param request 请求
     * @param response 相应
     * @throws ServletException 异常
     * @throws IOException 异常
     */
}

package com.tian.servlet;

import com.tian.config.SpringConfig;
import com.tian.domain.Atm;
import com.tian.domain.User;
import com.tian.service.AtmService;
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

@WebServlet({"/function/panel", "/function/editBalance"})
public class FunctionServlet extends HttpServlet {

    static AnnotationConfigApplicationContext ctx = null;

    static {
        ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("functionServlet的service函数开始执行。");

        if ("/function/panel".equals(request.getServletPath())) {
            // 调用控制台界面的函数。
            System.out.println("开始调用doOpenPanel函数。");
            doOpenPanel(request, response);
        } else if ("/function/editBalance".equals(request.getServletPath())) {
            // 编辑余额功能。
            System.out.println("开始调用编辑余额的函数：");
            doEditBalance(request, response);
        }
    }

    private void doOpenPanel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("开始执行doOpenPanel函数。");
        // 首先需要通过用户id查询到用户信息。
        int userid = (int)request.getSession().getAttribute("userid");
        System.out.println("获取到的userId="+userid);
        // 查询用户信息。
        UserServiceImpl userServiceImpl = ctx.getBean(UserServiceImpl.class);
        User user = userServiceImpl.selectUserById(userid);
        System.out.println("FunctionServlet执行doOpenPanel中获取到的user信息为：" + user);
        // 封装查询信息。
        System.out.println("FunctionServlet执行doOpenPanel中开始设置请求域对象。");
        // 将用户信息保存到session中。
        request.getSession().setAttribute("userInfo", user);
        // 重定向。
        System.out.println("FunctionServlet执行doOpenPanel中开始转发。");
        // request.getRequestDispatcher("/pages/panel.jsp").forward(request, response);
        // request.getRequestDispatcher("/pages/panel.jsp").forward(request, response);
        response.sendRedirect(request.getContextPath()+"/pages/panel.jsp");
    }

    /**
     * 修改用户余额功能。
     *
     * @param request  请求
     * @param response 相应
     */
    private void doEditBalance(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FunctionServlet调用editBalance函数。");
        // 获取用户id和待修改金额。
        HttpSession session = request.getSession(); // 获取session
        User userInfo = (User)session.getAttribute("userInfo"); // 获取用户信息。
        String atmid = (String)session.getAttribute("atmid");// 获取atm机器id。
        int changedBalance = Integer.parseInt(request.getParameter("changedBalance")); // 获取金额该变量。
        String direction = request.getParameter("direction"); // 获取金额流向。

        System.out.println("user = " + userInfo
                + "; atmid = " + atmid
                + "; changedBalance = " + changedBalance
                + "; direction = " + direction); // 打印获取结果。

        // 获取服务层实现类。
        UserServiceImpl userServiceImpl = ctx.getBean(UserServiceImpl.class);
        AtmService atmService = ctx.getBean(AtmServiceImpl.class);

        // 获取用户详细信息。
        int userId = userInfo.getUserId();
        int balance = userInfo.getBalance();

        // 获取atm详细信息。
        Atm atm = atmService.selectAtmInfo(Integer.parseInt(atmid));
        int atmBalance = atm.getAtmBalance();


        // 判断金额方向。
        if ("in".equals(direction)) {
            // 资金进入账户。
            // 无需检查，直接进入。
            // 通过业务层对象修改用户余额。
            userServiceImpl.addBalance(userId, changedBalance); // 修改账户金额。
            atmService.addBalance(Integer.parseInt(atmid), changedBalance); // 修改atm余额。
            // 重新获取用户信息并且添加到session。
            User newInfo = userServiceImpl.selectUserById(userId);
            session.setAttribute("userInfo", newInfo);
            // 重定向到panel页面。
            response.sendRedirect(request.getContextPath()+"/pages/panel.jsp");
        } else {
            // 资金离开账户
            // 判断账户资金是否充足
            if (balance >= changedBalance) {
                // 用户资金充足。
                // 判断atm余额是否充足。
                if (atmBalance >= changedBalance) {
                    // atm资金充足。
                    // 对用户扣款。（通过业务层实现类修改用户余额）
                    userServiceImpl.reduceBalance(userId, changedBalance);
                    // 重新获取用户信息并且添加到session。
                    User newInfo = userServiceImpl.selectUserById(userId);
                    session.setAttribute("userInfo", newInfo);
                    // 跳转回到控制台。
                    request.getRequestDispatcher("/pages/outSuccess.jsp").forward(request, response);
                    // response.sendRedirect(request.getContextPath()+"/pages/outSuccess.jsp");
                } else {
                    // atm资金不足。
                    // 跳转到atm资金不足界面。
                    response.sendRedirect(request.getContextPath() + "/pages/atmBalanceNotEnough.jsp");
                    // request.getRequestDispatcher(request.getContextPath() + "/pages/atmBalanceNotEnough.jsp").forward(request, response);
                }
            } else {
                // 用户资金不足。
                // 跳转到资金不足页面。
                response.sendRedirect(request.getContextPath() + "/pages/userBalanceNotEnough.jsp");
                // request.getRequestDispatcher(request.getContextPath() + "/pages/userBalanceNotEnough.jsp").forward(request, response);
            }
        }
/*        System.out.println("这段话来自于UserServlet.doEditBalance函数，修改余额用能已经基本结束。");
        // 页面跳转。
        request.getRequestDispatcher(request.getContextPath() + "/function/panel").forward(request, response);*/
    }

}

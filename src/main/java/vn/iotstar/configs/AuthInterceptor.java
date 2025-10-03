package vn.iotstar.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import vn.iotstar.entities.UserEntity;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("account");
        
        String uri = request.getRequestURI();
        
        // Cho phép truy cập các trang public
        if (uri.contains("/login") || uri.contains("/register") || 
            uri.contains("/logout") || uri.contains("/css") || 
            uri.contains("/js") || uri.contains("/images") ||
            uri.contains("/image")) {
            return true;
        }
        
        // Kiểm tra đăng nhập
        if (user == null) {
            session.setAttribute("redirectURL", uri);
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        
        int roleId = user.getRoleid();
        
        // Phân quyền theo URL
        if (uri.startsWith(request.getContextPath() + "/admin")) {
            // Chỉ admin (role = 3) mới được truy cập
            if (roleId != 3) {
                response.sendRedirect(request.getContextPath() + "/access-denied");
                return false;
            }
        } else if (uri.startsWith(request.getContextPath() + "/manager")) {
            // Chỉ manager (role = 2) mới được truy cập
            if (roleId != 2) {
                response.sendRedirect(request.getContextPath() + "/access-denied");
                return false;
            }
        } else if (uri.startsWith(request.getContextPath() + "/user")) {
            // Chỉ user (role = 1) mới được truy cập
            if (roleId != 1) {
                response.sendRedirect(request.getContextPath() + "/access-denied");
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Có thể thêm logic sau khi xử lý request
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // Có thể thêm logic sau khi hoàn thành request
    }
}
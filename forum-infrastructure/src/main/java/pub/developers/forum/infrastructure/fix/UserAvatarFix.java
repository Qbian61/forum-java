//package pub.developers.forum.infrastructure.fix;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import pub.developers.forum.common.support.SafesUtil;
//import pub.developers.forum.infrastructure.dal.dao.UserDAO;
//import pub.developers.forum.infrastructure.dal.dataobject.UserDO;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author Qiangqiang.Bian
// * @create 2021/5/5
// * @desc
// **/
//@Component
//public class UserAvatarFix {
//
//    @Resource
//    private UserDAO userDAO;
//
//    @Scheduled(cron = "0/30 * * * * ? ")
//    public void task() {
//        List<UserDO> userDOS = userDAO.query(UserDO.builder().build());
//        SafesUtil.ofList(userDOS).forEach(userDO -> {
//            if (!ObjectUtils.isEmpty(userDO.getAvatar())) {
//                String avatar = userDO.getAvatar();
//                userDO.setAvatar(avatar.replaceAll("www.gravatar.com", "sdn.geekzu.org"));
//
//                userDAO.update(userDO);
//            }
//        });
//    }
//}

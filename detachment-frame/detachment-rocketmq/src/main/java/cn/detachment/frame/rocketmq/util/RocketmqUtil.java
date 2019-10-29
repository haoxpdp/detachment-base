package cn.detachment.frame.rocketmq.util;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 16:34
 */
public class RocketmqUtil {

    public static RPCHook getRPCHookByAkSk(Environment env, String accessKeyOrExpr, String secretKeyOrExpr) {
        String ak, sk;
        try {
            ak = env.resolveRequiredPlaceholders(accessKeyOrExpr);
            sk = env.resolveRequiredPlaceholders(secretKeyOrExpr);
        } catch (Exception e) {
            // Ignore it
            ak = null;
            sk = null;
        }
        if (!StringUtils.isEmpty(ak) && !StringUtils.isEmpty(sk)) {
            return new AclClientRPCHook(new SessionCredentials(ak, sk));
        }
        return null;
    }

    public static String getInstanceName(RPCHook rpcHook, String identify) {
        String separator = "|";
        StringBuilder instanceName = new StringBuilder();
        SessionCredentials sessionCredentials = ((AclClientRPCHook) rpcHook).getSessionCredentials();
        instanceName.append(sessionCredentials.getAccessKey())
                .append(separator).append(sessionCredentials.getSecretKey())
                .append(separator).append(identify)
                .append(separator).append(UtilAll.getPid());
        return instanceName.toString();
    }
}

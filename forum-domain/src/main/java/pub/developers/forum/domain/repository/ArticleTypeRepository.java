package pub.developers.forum.domain.repository;

import pub.developers.forum.common.enums.ArticleTypeScopeEn;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.domain.entity.ArticleType;

import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
public interface ArticleTypeRepository {

    void save(ArticleType articleType);

    List<ArticleType> queryByState(AuditStateEn auditState);

    List<ArticleType> queryByScopesAndState(List<ArticleTypeScopeEn> scopes, AuditStateEn auditState);

    void updateAuditState(Long id, AuditStateEn auditState);

    ArticleType get(Long id);

    ArticleType getByNameAndState(String typeName, AuditStateEn pass);

    void increaseRefCount(Long id);

    void decreaseRefCount(Long id);

}

package pub.developers.forum.infrastructure;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.ArticleTypeScopeEn;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.exception.BizException;
import pub.developers.forum.domain.entity.ArticleType;
import pub.developers.forum.domain.repository.ArticleTypeRepository;
import pub.developers.forum.infrastructure.dal.dao.ArticleTypeDAO;
import pub.developers.forum.infrastructure.dal.dataobject.ArticleTypeDO;
import pub.developers.forum.infrastructure.transfer.ArticleTypeTransfer;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@Repository
public class ArticleTypeRepositoryImpl implements ArticleTypeRepository {

    @Resource
    private ArticleTypeDAO articleTypeDAO;

    @Override
    public void save(ArticleType articleType) {
        ArticleTypeDO articleTypeDO = ArticleTypeTransfer.toArticleTypeDO(articleType);

        try {
            articleTypeDAO.insert(articleTypeDO);
            articleType.setId(articleTypeDO.getId());
        } catch (DuplicateKeyException e) {
            throw new BizException(ErrorCodeEn.ARTICLE_TYPE_IS_EXIST);
        }
    }

    @Override
    public List<ArticleType> queryByState(AuditStateEn auditState) {
        List<ArticleTypeDO> articleTypeDOS = articleTypeDAO.query(ArticleTypeDO.builder()
                .auditState(ObjectUtils.isEmpty(auditState) ? null : auditState.getValue())
                .build());

        return ArticleTypeTransfer.toArticleTypes(articleTypeDOS);
    }

    @Override
    public List<ArticleType> queryByScopesAndState(List<ArticleTypeScopeEn> scopes, AuditStateEn auditState) {
        List<ArticleTypeDO> articleTypeDOS = articleTypeDAO.queryInScopesAndState(scopes.stream()
                .map(ArticleTypeScopeEn::getValue)
                .collect(Collectors.toList()), auditState.getValue());

        return ArticleTypeTransfer.toArticleTypes(articleTypeDOS);
    }

    @Override
    public void updateAuditState(Long id, AuditStateEn auditState) {
        ArticleTypeDO articleTypeDO = ArticleTypeDO.builder()
                .auditState(auditState.getValue())
                .build();
        articleTypeDO.setId(id);

        articleTypeDAO.update(articleTypeDO);
    }

    @Override
    public ArticleType get(Long id) {
        return ArticleTypeTransfer.toArticleType(articleTypeDAO.get(id));
    }

    @Override
    public ArticleType getByNameAndState(String typeName, AuditStateEn auditState) {
        List<ArticleTypeDO> articleTypeDOS = articleTypeDAO.query(ArticleTypeDO.builder()
                .name(typeName)
                .auditState(auditState.getValue())
                .build());

        if (ObjectUtils.isEmpty(articleTypeDOS)) {
            return null;
        }

        return ArticleTypeTransfer.toArticleType(articleTypeDOS.get(0));
    }

    @Override
    public void increaseRefCount(Long id) {
        articleTypeDAO.increaseRefCount(id);
    }

    @Override
    public void decreaseRefCount(Long id) {
        articleTypeDAO.decreaseRefCount(id);
    }
}

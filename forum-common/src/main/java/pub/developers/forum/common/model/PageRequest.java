package pub.developers.forum.common.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qiangqiang.Bian
 * @create 2020/8/3
 * @desc
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest<T> {
    private static final Integer DEF_PAGE_SIZE = 10;
    private static final Integer DEF_PAGE_NO = 1;

    private Integer pageSize = DEF_PAGE_SIZE;
    private Integer pageNo = DEF_PAGE_NO;
    private T filter;

    public static <T> PageRequest<T> build(Integer pageNo, Integer pageSize, T filter) {
        PageRequest<T> pageRequest = new PageRequest<>();
        pageRequest.setPageNo(pageNo);
        pageRequest.setPageSize(pageSize);
        pageRequest.setFilter(filter);

        return pageRequest;
    }

    public static PageRequest build(Integer pageNo, Integer pageSize) {
        PageRequest pageRequest = new PageRequest<>();
        pageRequest.setPageNo(pageNo);
        pageRequest.setPageSize(pageSize);

        return pageRequest;
    }

}

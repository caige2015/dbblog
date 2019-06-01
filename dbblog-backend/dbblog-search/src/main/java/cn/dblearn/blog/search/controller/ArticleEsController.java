package cn.dblearn.blog.search.controller;

import cn.dblearn.blog.common.Result;
import cn.dblearn.blog.common.constants.RabbitMqConstants;
import cn.dblearn.blog.entity.article.Article;
import cn.dblearn.blog.portal.article.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ArticleEsController
 *
 * @author bobbi
 * @date 2019/03/13 15:04
 * @email 571002217@qq.com
 * @description
 */
@RestController
@Slf4j
public class ArticleEsController {

    @Resource
    private ArticleService articleService;

    /**
     * 搜索标题，描述，内容
     * @param keywords
     * @return
     */
    @GetMapping("articles/search")
    public Result search(@RequestParam("keywords") String keywords){
        // 对所有索引进行搜索
        //Iterable转list
        List<Article> articleList=
                articleService.list(new QueryWrapper<Article>().lambda().eq(Article::getPublish,true));
        return Result.ok().put("articleList",articleList);
    }

    @RabbitListener(queues=RabbitMqConstants.REFRESH_ES_INDEX_QUEUE)
    public void refresh(String msg) {
        log.info(msg);
    }

}

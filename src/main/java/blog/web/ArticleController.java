package blog.web;


import blog.domain.Article;
import blog.domain.Comment;
import blog.service.impl.ArticleServiceImpl;
import blog.service.impl.CommentServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class ArticleController {
    private final static Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    public CommentServiceImpl commentService;

    @RequestMapping("/article")
    public ModelAndView detail(HttpServletRequest request){

        int id=Integer.parseInt(request.getParameter("id"));
        List<Comment> comments=commentService.allComments(id,0,10);

        Article article=articleService.selectById(id);
        Article lastArticle=articleService.selectLastArticle(id);
        Article nextArticle=articleService.selectNextArticle(id);

        Integer clickNum=article.getClick();
        article.setClick(clickNum+1);
        articleService.updateArticle(article);

        ModelAndView modelAndView=new ModelAndView("detail");
        modelAndView.addObject("article",article);
        modelAndView.addObject("comments",comments);
        modelAndView.addObject("lastArticle",lastArticle);
        modelAndView.addObject("nextArticle",nextArticle);
        return modelAndView;
    }
    @RequestMapping("/admin/article/detail")
    public ModelAndView adminArticleDetail(HttpServletRequest request){
        int id=Integer.parseInt(request.getParameter("id"));
        Article article=articleService.selectById(id);

        ModelAndView modelAndView=new ModelAndView("/admin/article_detail");
        modelAndView.addObject("article",article);


        return modelAndView;
    }
    @RequestMapping("/admin/article/comment")
    public ModelAndView adminArticleComment(HttpServletRequest request){
        int id=Integer.parseInt(request.getParameter("id"));
        List<Comment> comments=commentService.allComments(id,0,10);
        ModelAndView modelAndView=new ModelAndView("/admin/comment_list");
        modelAndView.addObject("comments",comments);
        return modelAndView;
    }
    @RequestMapping("/admin/article/list")
    public ModelAndView articleList(@RequestParam(required=true,defaultValue="1") Integer page, @RequestParam(required=false,defaultValue="10") Integer pageSize){
        PageHelper.startPage(page, pageSize);
        List<Article> articles=articleService.queryAll();
        PageInfo<Article> pageInfo=new PageInfo<Article>(articles);
        ModelAndView modelAndView=new ModelAndView("/admin/article_list");
        modelAndView.addObject("articles",articles);
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }
    @RequestMapping("/admin/article/add")
    public ModelAndView articleAdd(HttpServletRequest request,HttpServletResponse resp) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        ModelAndView modelAndView=new ModelAndView("/admin/article_add");

        return modelAndView;
    }
    @RequestMapping("/admin/article/add/do")
    public String articleAddDo(HttpServletRequest request, HttpServletResponse resp,RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        Article article=new Article();
        article.setTitle(request.getParameter("title"));
        article.setCatalogId(Integer.parseInt(request.getParameter("catalogId")));
        article.setKeywords(request.getParameter("keywords"));
        article.setdesci(request.getParameter("desci"));
        article.setContent(request.getParameter("content"));
        article.setTime(new Date());
        logger.info(article.getTitle());
        logger.info(article.getdesci());
        if (articleService.insert(article)){
            redirectAttributes.addFlashAttribute("succ", "发表文章成功！");
            return "redirect:/admin/article/add";
        }else {
            redirectAttributes.addFlashAttribute("error", "发表文章失败！");
            return "redirect:/admin/article/add";
        }
    }
    @RequestMapping(value = "/admin/article/search")
    public ModelAndView articleSearch(HttpServletRequest request){
        String word=request.getParameter("word");
        List<Article> articles=articleService.selectByWord(word);

        ModelAndView modelAndView=new ModelAndView("/admin/article_list");
        modelAndView.addObject("articles",articles);
        return modelAndView;
    }
    @RequestMapping(value = "/admin/article/edit")
    public ModelAndView articleEdit(HttpServletRequest request,HttpServletResponse resp) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        int id=Integer.parseInt(request.getParameter("id"));
        Article article=articleService.selectById(id);
        ModelAndView modelAndView=new ModelAndView("/admin/article_edit");
        modelAndView.addObject("article",article);
        return modelAndView;
    }
    @RequestMapping(value = "/admin/article/edit/do")
    public ModelAndView articleEditDo(HttpServletRequest request,HttpServletResponse resp) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        Article article=new Article();
        article.setId(Integer.parseInt(request.getParameter("id")));
        article.setTitle(request.getParameter("title"));
        logger.info(article.getTitle());
        article.setCatalogId(Integer.parseInt(request.getParameter("catalogId")));
        article.setKeywords(request.getParameter("keywords"));
        article.setdesci(request.getParameter("desci"));
        article.setContent(request.getParameter("content"));
        ModelAndView modelAndView=new ModelAndView("/admin/article_edit");
        if (articleService.updateArticle(article)){
            modelAndView.addObject("succ", "修改文章成功！");

        }else {
            modelAndView.addObject("error", "修改文章失败！");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/api/article/del", method = RequestMethod.POST)
    public @ResponseBody Object loginCheck(HttpServletRequest request) {
        int id=Integer.parseInt(request.getParameter("id"));
        int result=articleService.deleteById(id);
        HashMap<String, String> res = new HashMap<String, String>();
        if (result==1){
            res.put("stateCode", "1");
        }else {
            res.put("stateCode", "0");
        }
        return res;
    }
}

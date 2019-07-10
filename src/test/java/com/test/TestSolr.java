package com.test;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建人 JianBo Zhang
 * @创建时间 2019-05-19
 * @描述
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class TestSolr {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testadd(){
        TbItem item = new TbItem();
        item.setId(1L);//存在修改不存在添加
        item.setTitle("华为mete10");
        item.setCategory("手机");
        item.setBrand("华为");
        item.setSeller("官方旗舰店");
        item.setGoodsId(1L);
        item.setPrice(new BigDecimal(3000.00));

        solrTemplate.saveBean(item);
        solrTemplate.commit();//一定提交
    }

    @Test
    public void findById(){
        TbItem byId = solrTemplate.getById(1L, TbItem.class);
        System.out.println(byId.getTitle());

    }


    @Test
    public void deleteById(){
        solrTemplate.deleteById("2");

        solrTemplate.commit();
    }

    @Test
    public void testaddList(){
        List list = new ArrayList();
        for(int i=0;i<50;i++){
            TbItem item = new TbItem();
            item.setId(i+1L);//存在修改不存在添加
            item.setTitle("华为mete10"+i);
            item.setCategory("手机");
            item.setBrand("华为");
            item.setSeller("官方旗舰店");
            item.setGoodsId(i+1L);
            item.setPrice(new BigDecimal(3000.00+i));
            list.add(item);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();//一定提交
    }

    @Test
    public void testPageQuery(){
        Query query = new SimpleQuery("*:*");
        query.setOffset(20);//起始的索引
        query.setRows(10);//每页查多少条

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        for(TbItem item : page.getContent()){
            System.out.println(item.getTitle()+"    "+item.getPrice()+"   "+item.getBrand());
        }

        System.out.println("总记录数:"+page.getTotalElements());
        System.out.println("总页数："+page.getTotalPages());



    }

    @Test
    public void testPageQueryMutil(){
        Query query = new SimpleQuery("*:*");
        Criteria criteria= new Criteria("item_category").contains("手机");
        query.addCriteria(criteria);
        query.setOffset(20);//起始的索引
        query.setRows(10);//每页查多少条

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        for(TbItem item : page.getContent()){
            System.out.println(item.getTitle()+"    "+item.getPrice()+"   "+item.getBrand());
        }

        System.out.println("总记录数:"+page.getTotalElements());
        System.out.println("总页数："+page.getTotalPages());



    }

    @Test
    public void deleteAll(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

}

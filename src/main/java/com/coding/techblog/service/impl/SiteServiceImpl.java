package com.coding.techblog.service.impl;

import com.coding.techblog.constant.WebConst;
import com.coding.techblog.controller.admin.AttachController;
import com.coding.techblog.dao.AttachVoMapper;
import com.coding.techblog.dao.CommentVoMapper;
import com.coding.techblog.dao.ContentVoMapper;
import com.coding.techblog.dao.MetaVoMapper;
import com.coding.techblog.dto.MetaDto;
import com.coding.techblog.dto.Types;
import com.coding.techblog.exception.TipException;
import com.coding.techblog.modal.Bo.ArchiveBo;
import com.coding.techblog.modal.Bo.BackResponseBo;
import com.coding.techblog.modal.Bo.StatisticsBo;
import com.coding.techblog.modal.Vo.*;
import com.coding.techblog.service.ISiteService;
import com.coding.techblog.utils.DateKit;
import com.coding.techblog.utils.TaleUtils;
import com.coding.techblog.utils.ZipUtils;
import com.coding.techblog.utils.backup.Backup;
import com.github.pagehelper.PageHelper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;


@Service
public class SiteServiceImpl implements ISiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Resource
    private CommentVoMapper commentDao;

    @Resource
    private ContentVoMapper contentDao;

    @Resource
    private AttachVoMapper attachDao;

    @Resource
    private MetaVoMapper metaDao;

    @Override
    public List<CommentVo> recentComments(int limit) {
        LOGGER.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        CommentVoExample example = new CommentVoExample();
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<CommentVo> byPage = commentDao.selectByExampleWithBLOBs(example);
        LOGGER.debug("Exit recentComments method");
        return byPage;
    }

    @Override
    public List<ContentVo> recentContents(int limit) {
        LOGGER.debug("Enter recentContents method");
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        ContentVoExample example = new ContentVoExample();
        example.createCriteria().andStatusEqualTo(Types.PUBLISH.getType()).andTypeEqualTo(Types.ARTICLE.getType());
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<ContentVo> list = contentDao.selectByExample(example);
        LOGGER.debug("Exit recentContents method");
        return list;
    }

    @Override
    public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
        BackResponseBo backResponse = new BackResponseBo();
        if (bk_type.equals("attach")) {
            if (StringUtils.isBlank(bk_path)) {
                throw new TipException("Vui lòng nhập đường dẫn tệp sao lưu");
            }
            if (!(new File(bk_path)).isDirectory()) {
                throw new TipException("Vui lòng nhập đường dẫn thư mục");
            }
            String bkAttachDir = AttachController.CLASSPATH + "upload";
            String bkThemesDir = AttachController.CLASSPATH + "templates/themes";

            String fname = DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".zip";

            String attachPath = bk_path + "/" + "attachs_" + fname;
            String themesPath = bk_path + "/" + "themes_" + fname;

            ZipUtils.zipFolder(bkAttachDir, attachPath);
            ZipUtils.zipFolder(bkThemesDir, themesPath);

            backResponse.setAttachPath(attachPath);
            backResponse.setThemePath(themesPath);
        }
        if (bk_type.equals("db")) {

            String bkAttachDir = AttachController.CLASSPATH + "upload/";
            if (!(new File(bkAttachDir)).isDirectory()) {
                File file = new File(bkAttachDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            String sqlFileName = "tale_" + DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".sql";
            String zipFile = sqlFileName.replace(".sql", ".zip");

            Backup backup = new Backup(TaleUtils.getNewDataSource().getConnection());
            String sqlContent = backup.execute();

            File sqlFile = new File(bkAttachDir + sqlFileName);
            write(sqlContent, sqlFile, Charset.forName("UTF-8"));

            String zip = bkAttachDir + zipFile;
            ZipUtils.zipFile(sqlFile.getPath(), zip);

            if (!sqlFile.exists()) {
                throw new TipException("Sao lưu cơ sở dữ liệu không thành công");
            }
            sqlFile.delete();

            backResponse.setSqlPath(zipFile);


            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new File(zip).delete();
                }
            }, 10 * 1000);
        }
        return backResponse;
    }

    @Override
    public CommentVo getComment(Integer coid) {
        if (null != coid) {
            return commentDao.selectByPrimaryKey(coid);
        }
        return null;
    }

    @Override
    public StatisticsBo getStatistics() {
        LOGGER.debug("Enter getStatistics method");
        StatisticsBo statistics = new StatisticsBo();

        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        Long articles =   contentDao.countByExample(contentVoExample);

        Long comments = commentDao.countByExample(new CommentVoExample());

        Long attachs = attachDao.countByExample(new AttachVoExample());

        MetaVoExample metaVoExample = new MetaVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(Types.LINK.getType());
        Long links = metaDao.countByExample(metaVoExample);

        statistics.setArticles(articles);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setLinks(links);
        LOGGER.debug("Exit getStatistics method");
        return statistics;
    }

    @Override
    public List<ArchiveBo> getArchives() {
        LOGGER.debug("Enter getArchives method");
        List<ArchiveBo> archives = contentDao.findReturnArchiveBo();
        if (null != archives) {
            archives.forEach(archive -> {
                ContentVoExample example = new ContentVoExample();
                ContentVoExample.Criteria criteria = example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
                example.setOrderByClause("created desc");
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyythangMMnam");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
                criteria.andCreatedGreaterThan(start);
                criteria.andCreatedLessThan(end);
                List<ContentVo> contentss = contentDao.selectByExample(example);
                archive.setArticles(contentss);
            });
        }
        LOGGER.debug("Exit getArchives method");
        return archives;
    }

    @Override
    public List<MetaDto> metas(String type, String orderBy, int limit){
        LOGGER.debug("Enter metas method:type={},order={},limit={}", type, orderBy, limit);
        List<MetaDto> retList=null;
        if (StringUtils.isNotBlank(type)) {
            if(StringUtils.isBlank(orderBy)){
                orderBy = "count desc, a.mid desc";
            }
            if(limit < 1 || limit > WebConst.MAX_POSTS){
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderBy);
            paraMap.put("limit", limit);
            retList= metaDao.selectFromSql(paraMap);
        }
        LOGGER.debug("Exit metas method");
        return retList;
    }


    private void write(String data, File file, Charset charset) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes(charset));
        } catch (IOException var8) {
            throw new IllegalStateException(var8);
        } finally {
            if(null != os) {
                try {
                    os.close();
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }
        }

    }

}

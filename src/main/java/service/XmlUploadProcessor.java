package service;

import domain.META_DATA;
import domain.PAGE;
import domain.TREE;
import domain.TREE_NODE;
import domain.util.JAXBUtils;
import domain.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class XmlUploadProcessor implements ItemProcessor<BatchContext,String> {
    private static Logger logger = LoggerFactory.getLogger(XmlUploadProcessor.class);

    @Override
    public String process(BatchContext batchContext) throws Exception {
        Set<String> uploadedNames = batchContext.getUploadedNameList();
        String app_no = "C8037" + TimeUtils.getCurrentSeconds() + UUID.randomUUID().toString().replaceAll("-","").substring(30);

        List<PAGE> PAGEList = new ArrayList<>();
        for (String fileName : uploadedNames){
            PAGE page = new PAGE();
            page.setACTION("add");
            page.setOPERATE_TIME(TimeUtils.getSystemTime());
            page.setFILENAME(fileName);
            page.setTYPE("01");

            PAGEList.add(page);
        }

        TREE tree = new TREE();
        tree.setID("01");
        tree.setNAME("身份证明");
        tree.setPAGE(PAGEList);

        TREE_NODE tree_node = new TREE_NODE();
        tree_node.setTREE(tree);

        META_DATA meta_data = new META_DATA();
        meta_data.setAPP_CODE("CREDIT");
        meta_data.setAPP_NO(app_no);
        meta_data.setCASE_NO("Y017121514192646");
        meta_data.setTREE_NODE(tree_node);

        String xml = JAXBUtils.convertToXml(meta_data);

        logger.info(xml);

        return xml;
    }
}
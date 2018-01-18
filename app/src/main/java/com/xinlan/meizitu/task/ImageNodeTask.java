package com.xinlan.meizitu.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.xinlan.meizitu.config.Constant;
import com.xinlan.meizitu.data.Node;
import com.xinlan.meizitu.data.Resource;
import com.xinlan.meizitu.data.Trans;
import com.xinlan.meizitu.util.UrlUtil;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panyi on 2017/8/9.
 */

public class ImageNodeTask extends AsyncTask<String, Void, Integer> {
    private Node root;

    public ImageNodeTask(Node root) {
        this.root = root;
        root.setChildList(new ArrayList<Node>(10));
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        //System.out.println("url = "+url);
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(Constant.UA).get();
            //System.out.println(doc.body().toString());
            Node firstNode = readMainImage(doc, url);
            if (firstNode == null)
                return -1;

            readPageInfo(doc, firstNode);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        EventBus.getDefault().post(new Trans(Constant.CMD_NODE_LIST_GET));
    }

    private Node readMainImage(Document doc, final String url) {
        if (doc == null)
            return null;

        Elements elements = doc.getElementsByClass("main-image");
        if (elements == null || elements.size() == 0)
            return null;
        Element elem = elements.get(0);
        Elements imgs = elem.getElementsByTag("img");
        if (imgs != null && imgs.size() > 0) {
            String firstImageUrl = imgs.get(0).absUrl("src");
            Node node = new Node();
            node.setRefer(url);
            node.setImage(firstImageUrl);

            //System.out.println("image = " + firstImageUrl);
            //root.getChildList().add(node);
            return node;
        }

        return null;
    }

    private void readPageInfo(Document doc, Node firstNode) {
        if (doc == null || firstNode == null)
            return;

        Elements pageElems = doc.getElementsByClass("pagenavi");
        if (pageElems == null || pageElems.size() == 0)
            return;


        Element pageElem = pageElems.get(0);
        Elements childs = pageElem.children();
        int totalNum = 0;
        String link = null;
        for (int i = 0, len = childs.size(); i < len; i++) {
            try {
                Element childNode = childs.get(i);
                //System.out.println(childNode);
                if (TextUtils.equals(childNode.tagName(), "a")) {
                    Elements as = childNode.getElementsByTag("a");
                    Element aElem = as.get(0);
                    link = aElem.absUrl("href");
                    //System.out.println("link = " + link);
                    Elements spans = aElem.getElementsByTag("span");
                    Element span = spans.get(0);
                    //System.out.println("span = " + span.html());
                    if (TextUtils.isDigitsOnly(span.html())) {
                        int spanVal = Integer.parseInt(span.html());
                        if (spanVal > totalNum) {
                            totalNum = spanVal;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//end for i

        //System.out.println("total = " + totalNum);
        //System.out.println("result link = " + link);

        String imageBaseUrl = UrlUtil.findUrlWithOutSufix(firstNode.getImage());
        String suffix = UrlUtil.findUrlSufix(firstNode.getImage());
        String imageFormat = UrlUtil.getImageFormatStr(suffix, imageBaseUrl);

        String linkBase = UrlUtil.findUrlWithOutSufix(link);
        String linkFormat = linkBase+"/%s";

        List<Node> nodes = new ArrayList<Node>();
        for(int i = 1;i<=totalNum;i++){
            String refer = String.format(linkFormat,i);
            String imageUrl = null;
            if(i<10){
                imageUrl = String.format(imageFormat,"0"+i);
            }else{
                imageUrl = String.format(imageFormat,i);
            }

            Node imageNode=new Node();
            imageNode.setImage(imageUrl);
            imageNode.setRefer(refer);
            nodes.add(imageNode);
            //System.out.println("refer = " + refer);
            //System.out.println("imageUrl = " + imageUrl);
        }//end for i

        if (nodes.size() > 0) {
            root.getChildList().addAll(nodes);
        }
    }

}//end class

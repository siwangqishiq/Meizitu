package com.xinlan.meizitu.task;

import android.os.AsyncTask;

import com.xinlan.meizitu.Constant;
import com.xinlan.meizitu.data.Node;
import com.xinlan.meizitu.data.Resource;
import com.xinlan.meizitu.data.Trans;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by panyi on 2017/8/9.
 */

public class FindRootNodeTask extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(Constant.UA).get();
            //System.out.println(doc.body().toString());
            readNodeList(url, doc);
            readNextUrl(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        EventBus.getDefault().post(new Trans(Constant.CMD_REFRESH_ROOTLIST));
    }

    /**
     * 根节点列表 读取
     *
     * @param doc
     * @return
     * @throws Exception
     */
    private int readNodeList(String url, Document doc) throws Exception {
        if (doc == null)
            return -1;

        Elements postList = doc.getElementsByClass("postlist");
        if (postList.size() == 0)
            return -1;

        Element contentElem = postList.get(0);
        Elements list = contentElem.getElementsByTag("li");
        for (Element liElem : list) {
            Node node = new Node();
            node.setRefer(url);

            Elements aList = liElem.getElementsByTag("a");
            if (aList.size() > 0) {
                Element aTag = aList.get(0);
                String link = aTag.absUrl("href");
                //System.out.println("link ----> "+link);
                node.setLink(link);
            }

            Elements imgList = liElem.getElementsByTag("img");
            if (imgList.size() > 0) {
                Element imgTag = imgList.get(0);
                String title = imgTag.attr("alt");
                //System.out.println("title ----> "+title);
                node.setTitle(title);
                String imageUrl = imgTag.absUrl("data-original");
                //System.out.println("imageUrl ----> "+imageUrl);
                node.setImage(imageUrl);
            }
            Resource.getInstance().addRootList(node);
            //System.out.println("sdsa--->"+liElem.toString());
        }//end for each li tag
        return 0;
    }

    private int readNextUrl(Document doc) {
        if (doc == null)
            return -1;

        Elements ret = doc.getElementsByClass("next page-numbers");
        if (ret.size() == 0)
            return 0;
        Element elem = ret.get(0);

        String nextHref = elem.absUrl("href");
        //System.out.println("next ---->"+nextHref);
        Resource.getInstance().setNextPage(nextHref);
        return 0;
    }


}//end class

package com.example.admin.nav;

import com.example.admin.nav.model.ListImageData;
import com.example.admin.nav.model.ReceiveImageData;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        List<ReceiveImageData> list = new ArrayList<>();
        list.add(new ReceiveImageData(1L, "name 1", "body 1"));
        list.add(new ReceiveImageData(2L, "name 1", "body 1"));
        list.add(new ReceiveImageData(3L, "name 1", "body 1"));
        list.add(new ReceiveImageData(4L, "name 1", "body 1"));
        list.add(new ReceiveImageData(5L, "name 1", "body 1"));
        list.add(new ReceiveImageData(6L, "name 1", "body 1"));

        ListImageData listImageData = new ListImageData();
        listImageData.setList(list);
        String json = new Gson().toJson(listImageData);
        List<ReceiveImageData> rs = new Gson().fromJson(json, ListImageData.class).getList();
    }
}
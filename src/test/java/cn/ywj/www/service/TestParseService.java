package cn.ywj.www.service;//package cn.ywj.www.service;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import javax.naming.directory.NoSuchAttributeException;
//import java.util.Arrays;
//
//public class TestParseService {
//
//    private  String str = "端午节大调查§§0§;§radio§;§〒§你喜欢什么口味的粽子?§〒甜味粽〒咸味粽〒辣味粽§¤§§;§radio§;§〒§你吃过什么味的粽子？§〒甜味粽〒咸味粽〒辣味粽";
//
//    @Test
//    public void shouldReturnTitle() {
//        String title = "端午节大调查";
//        ParseService ps = new ParseService(str);
//        Assert.assertEquals(title, ps.parseQnTitle());
//    }
//
//
//    @Test
//    public void shouldSizeEqualTwo() {
//
//        ParseService ps = new ParseService(str);
//        ps.parseQnTitle();
//        Assert.assertEquals(2, ps.parseQnItem().length);
//    }
//
//
//    @Test
//    public void shouldGetItemType() {
//        ParseService ps = new ParseService(str);
//        ps.parseQnTitle();
//        for (String s : ps.parseQnItem()) {
//            try {
//
//                System.out.println(ps.parseItemType(s));
//            } catch (NoSuchAttributeException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Test
//    public void shouldGetItemTitle() {
//        ParseService ps = new ParseService(str);
//        ps.parseQnTitle();
//        for (String s : ps.parseQnItem()) {
//            try {
//                String ss = ps.parseItemType(s);
////                System.out.println(ss);
//                String str = s.substring(ss.length() + 6);
//                System.out.println(ps.parseItemTitle(str));
//            } catch (NoSuchAttributeException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    @Test
//    public void shouldGetItemOption() {
//        ParseService ps = new ParseService(str);
//        ps.parseQnTitle();
//        for (String s : ps.parseQnItem()) {
//            try {
//                String ss = ps.parseItemType(s);
//                String str = s.substring(ss.length() + 6);
//                String t = ps.parseItemTitle(str);
//                str = str.substring(t.length()+4);
//
//                for (String  as :  ps.parseItemOption(str)) {
//                    System.out.println(as);
//                }
//            } catch (NoSuchAttributeException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//}

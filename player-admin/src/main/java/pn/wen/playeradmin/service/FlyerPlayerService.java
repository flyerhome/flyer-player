package pn.wen.playeradmin.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pn.wen.playeradmin.api.Range;
import pn.wen.playeradmin.common.FlyerResult;
import pn.wen.playeradmin.dto.FlyerDownloadDto;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class FlyerPlayerService {
    private final static String STORAGE_PATH = "F:\\study\\m3u8";


    public FlyerResult<?> download(FlyerDownloadDto dto) {
        /*String url = dto.getUrl();
        List<String> tsList = getTsList(url);
        return FlyerResult.success(tsList);*/
        return null;
    }
/*

    private List<String> saveM3u8(FlyerDownloadDto dto) {
        String url = dto.getUrl();
        String name = dto.getName();
        String indexM3u8String = HttpUtil.get(url);
        if (indexM3u8String == null || indexM3u8String.trim().isEmpty()) {
            return new ArrayList<>();
        }
        File path = new File(STORAGE_PATH, name);
        if (!path.exists()) {
            path.mkdirs();
        }
        List<String> tsList = new ArrayList<>();
        List<String> m3u8List = new ArrayList<>();
        String[] split = indexM3u8String.split("\n");
        for (String s : split) {
            if (s.endsWith(".ts")) {
                tsList.add(s);
            }
            if (s.endsWith(".m3u8")) {
                m3u8List.add(s);
            }
        }
        if (!tsList.isEmpty()) {
            return tsList;
        }
        if (m3u8List.isEmpty()) {
            return new ArrayList<>();
        }
        String mixedM3u8 = m3u8List.get(0);
        String prefix = url.substring(0, url.lastIndexOf("/") + 1);
        String mixedM3u8Url = prefix + mixedM3u8;
        indexM3u8String = indexM3u8String.replace(mixedM3u8, "");
        File indexFile = new File(STORAGE_PATH, "index.m3u8");
        FileUtil.writeString(indexM3u8String, indexFile, CharsetUtil.CHARSET_UTF_8);

    }

    private List<String> getTsList(String url) {
        String result = HttpUtil.get(url);
        System.out.println("result = " + result);
        List<String> list = new ArrayList<>();
        if (result == null || result.trim().isEmpty()) {
            return list;
        }
        // 多种
        //#EXTM3U
        //#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=800000,RESOLUTION=1080x608
        //1000k/hls/mixed.m3u8
        String[] split = result.split("\n");
        List<String> m3u8List = new ArrayList<>();
        for (String s : split) {
            if (s.endsWith(".ts")) {
                list.add(s);
            }
            if (s.endsWith(".m3u8")) {
                m3u8List.add(s);
            }
        }
        url = url.substring(0, url.lastIndexOf("/") + 1);
        url = url + m3u8List.get(0);
        result = HttpUtil.get(url);
        if (result == null || result.trim().isEmpty()) {
            return list;
        }
        String[] split2 = result.split("\n");
        url = url.substring(0, url.lastIndexOf("/") + 1);
        for (String s : split2) {
            if (s.endsWith(".ts")) {
                list.add(url + s);
            }
        }
        return list;
    }
*/


    public ResponseEntity<byte[]> fileName(@PathVariable String fileName, HttpServletRequest request) {
        // 1. 拼接文件完整路径，防止路径遍历攻击
        File videoFile = new File(STORAGE_PATH, fileName);
        // 校验文件存在性与合法性
        if (!videoFile.exists() || !videoFile.isFile() || !videoFile.canRead()) {
            return ResponseEntity.notFound().build();
        }

        try {
            long fileTotalLength = videoFile.length();
            // 2. 解析Range请求头
            Range range = Range.parse(request, fileTotalLength);
            long start = range.getStart();
            long end = range.getEnd();
            long contentLength = end - start + 1;

            // 3. 读取指定字节范围的视频数据
            byte[] videoChunk = new byte[(int) contentLength];
            try (RandomAccessFile raf = new RandomAccessFile(videoFile, "r")) {
                // 跳转到起始位置
                raf.seek(start);
                // 读取指定长度数据
                raf.readFully(videoChunk);
            }

            // 4. 构建响应头（分片传输核心标准头）
            HttpHeaders headers = new HttpHeaders();
            // 内容类型：通用视频类型，可根据文件后缀精准匹配
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 分片范围：bytes 起始-结束/总长度
            headers.add("Content-Range", String.format("bytes %d-%d/%d", start, end, fileTotalLength));
            // 当前分片的字节长度
            headers.add("Content-Length", String.valueOf(contentLength));
            // 支持跨域（前端跨域访问时需要）
            headers.add("Access-Control-Allow-Origin", "*");
            // 声明支持Range请求
            headers.add("Accept-Ranges", "bytes");

            // 5. 返回206状态码（HTTP 206 Partial Content 表示部分内容响应）
            return new ResponseEntity<>(videoChunk, headers, HttpStatus.PARTIAL_CONTENT);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public FlyerResult<List<FlyerDownloadDto>> list() {
        File dir = new File(STORAGE_PATH);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return FlyerResult.success();
        }
        List<File> fileList = Arrays.stream(files).sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).toList();
        List<FlyerDownloadDto> result = new ArrayList<>();
        for (File file : fileList) {
            String fileName = file.getName();
            FlyerDownloadDto dto = new FlyerDownloadDto();
            dto.setName(fileName);
            dto.setUrl("/hls/" + fileName + "/index.m3u8");
            result.add(dto);
        }
        return FlyerResult.success(result);
    }
}

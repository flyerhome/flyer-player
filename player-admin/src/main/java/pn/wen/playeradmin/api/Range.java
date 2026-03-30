package pn.wen.playeradmin.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class Range {
    /** 分片起始位置 */
    private long start;
    /** 分片结束位置 */
    private long end;
    /** 视频总长度 */
    private long total;

    /**
     * 解析Http请求中的Range头
     * @param request 请求对象
     * @param totalLength 文件总字节数
     * @return 解析后的Range对象
     */
    public static Range parse(HttpServletRequest request, long totalLength) {
        Range range = new Range();
        range.setTotal(totalLength);
        // 默认读取全部文件
        range.setStart(0);
        range.setEnd(totalLength - 1);

        String rangeHeader = request.getHeader(HttpHeaders.RANGE);
        // 无Range头，直接返回完整文件
        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
            return range;
        }

        // 解析 bytes=start-end 格式
        String[] ranges = rangeHeader.replace("bytes=", "").split("-");
        try {
            // 解析起始位置
            if (ranges[0].length() > 0) {
                range.setStart(Long.parseLong(ranges[0]));
            }
            // 解析结束位置（可选，不传则默认到文件末尾）
            if (ranges.length > 1 && ranges[1].length() > 0) {
                range.setEnd(Long.parseLong(ranges[1]));
            }
            // 边界校验：结束位置不能超过文件总长度-1
            if (range.getEnd() > totalLength - 1) {
                range.setEnd(totalLength - 1);
            }
            // 非法范围校验
            if (range.getStart() > range.getEnd()) {
                throw new IllegalArgumentException("非法的Range范围：起始位置大于结束位置");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Range头格式解析失败", e);
        }
        return range;
    }


    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

package cn.wmyskxz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wzh
 * @since 2022-02-16
 */
@Data
@Setter
@Getter
public class OrderQ implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer score;

    private String orderStatus;

    private Integer buyerId;

    private Integer sellerId;

    private Integer questionId;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createDate;

    private Float price;

    private Float suggestTime;

    private Integer buyerStatus;

    private Integer sellerStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(Float suggestTime) {
        this.suggestTime = suggestTime;
    }

    public Integer getBuyerStatus() {
        return buyerStatus;
    }

    public void setBuyerStatus(Integer buyerStatus) {
        this.buyerStatus = buyerStatus;
    }

    public Integer getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(Integer sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    @Override
    public String toString() {
        return "OrderQ{" +
                "id=" + id +
                ", score=" + score +
                ", orderStatus=" + orderStatus +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", questionId=" + questionId +
                ", createDate=" + createDate +
                ", price=" + price +
                ", suggestTime=" + suggestTime +
                ", buyerStatus=" + buyerStatus +
                ", sellerStatus=" + sellerStatus +
                "}";
    }
}

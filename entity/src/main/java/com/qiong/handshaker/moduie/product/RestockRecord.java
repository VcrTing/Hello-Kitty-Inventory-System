package com.qiong.handshaker.moduie.product;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qiong.handshaker.define.query.QDate;
import com.qiong.handshaker.moduie.BaseEntity;
import com.qiong.handshaker.moduie.base.Storehouse;
import com.qiong.handshaker.moduie.base.Supplier;
import com.qiong.handshaker.utils.basic.QTypedUtil;
import com.qiong.handshaker.vo.product.VoRestockPostForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@TableName("prod_restock_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockRecord extends BaseEntity {
    private Long product_sql_id;
    private Long supplier_sql_id;

    private Date restock_date;

    private BigDecimal restock_price;
    private BigDecimal lowest_price;
    private BigDecimal selling_price;

    private String restock_distribute;

    public static RestockRecord init(VoRestockPostForm form, Supplier supplier, Product product) {
        RestockRecord res = new RestockRecord();

        res.setRestock_date(QTypedUtil.serDate(form.getRestock_date(), true));

        res.setRestock_price(form.getRestock_price());
        res.setLowest_price(form.getLowest_price());
        res.setSelling_price(form.getSelling_price());

        res.setRestock_distribute(JSONUtil.toJsonStr(form.getRestock_distribute()));

        if (supplier != null) res.setSupplier_sql_id(supplier.getId());

        res.setProduct_sql_id(product.getId());

        return res;
    }
}

1. 收货-预检生成界面显示的数据（显示15天内未生成预检单)



call  pkg_yjd.p_report_yjd('00001',:cur_result);


cch:出库号   ckrq :出库日期    scbh:发货部门  pzs:品种数
dps:单品数  je:金额



2. 收货-预检生成界面（暂时不用）
输入配送部门，出车号

检测预检单是否可以生成

 pkg_yjd.p_jc_yjd('00001','99991','1287989');     ---商场号，bmbh，出库号


3. 收货-预检生成界面 --查询按钮（输入配送部门，出车号）


pkg_yjd.p_cx_yjd(as_scbh char,as_bmbh char,as_cch NUMBER,cur_result out ref_cur)

 调用事例如下：
call  pkg_yjd.p_cx_yjd('00001,'99991','1287989',:cur_result);


返回字段的意义:
cch:出库号   ckrq :出库日期    scbh:发货部门  pzs:品种数
dps:单品数  je:金额   zl:重量


4. 预检单界面，点击验货列表调用如下过程,不返回数据


生成预检单

 其中'11111111'为dxsrf 中的id ,A1 as_scbh,A2 as_bmbh , A3 出库号, A4 操作员  A5 单据状态  开始为null,处理了之后变为YCL

 begin
  pkg_yjd.p_sc_yjd('11111111','00001');
  end;


5. 验货列表界面显示（去审核）

 begin
pkg_yjd.p_cxyh_yjd(as_djdh number, as_scbh char,cur_result out ref_cur);
end;

返回字段的意义:
cch:出库号   ckrq :出库日期    scbh:发货部门  pzs:品种数
dps:单品数  je:金额   zl:重量

djdh:大件单号   glbh:管理编码   spbh:商品编码   spmc:商品名称  dhgg:订货规格
yss:应收数   ys:应收件数  spjj:商品进价  kcsl:库存数量c

sssl:实收数量
cysl:差异数量
cyjs:差异件数
djly:单据类型


6. 验货列表-点击验货 调用如下存储过程,不返回数据（最后审核）


          as_djdh  :大件单号
            as_scbh   :商场编号

begin
pkg_yjd.p_sh_yjd(as_djdh number, as_scbh char);
end;


7. 计算差异数量（不需要调用）

  * 用途: 计算差异数量
  *
  * 参数:
  *         as_djdh   : 前台传入字符串
            as_scbh   :商场编号
--uuid
--xgrq sysdate
--A1 djdh  大件单号
--A2 scbh   门店号
--A3 BMBH   配送部门
---A4 glbh  管理编号
---A5  sssl  ---实收数量

procedure p_get_cys(as_djdh char,as_scbh char)


8.收货
数据存本地rf枪，点击去审核后，本地计算差异，（用户可以返回进行修改删除），最后点审核后，数据传给服务器，插入dxsrf表

insert into dxsrf(uuid,xgrq,a1,a2,a3,a4,a5)  values(uuid,sysdate,djdh,scbh,bmbh,glbh,sssl)


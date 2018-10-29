--- 根据条码后六位查询【多个商品】
select a.glbh,b.spmc,a.spbh from xt_ftm a,xt_spda b where a.spbh like '%$keyword' and a.glbh = b.glbh

--- 根据管理编码查询商品明细[生鲜]
select b.spbh,a.dhzt,a.dhfs,a.shfs,f.spztmc,b.glbh,b.scbh,e.spcd,b.plu,b.spmc,b.spsj jzsj,b.hysj jzhyj,b.plulx
								from xt_lwxx a,xt_plu b,xt_ftm e,dm_spzt f
								where a.glbh = b.glbh and f.spzt = a.spzt and e.glbh = b.glbh and b.glbh = '1027094' and b.scbh = '00143';

-- 商品称重、金额【生鲜】
select a.mglbh,b.spmc from xt_zmm a,xt_spda b where a.zglbh = '1027094' and a.scbh = '00143' and b.glbh = a.mglbh

--- 根据条码查询商品明细[非生鲜]
select b.spbh,a.dhzt,a.dhfs,a.shfs,f.spztmc,a.glbh,a.scbh,b.spcd,c.spmc,a.jzsj,a.jzhyj
								from xt_lwxx a,xt_ftm b,xt_spda c,dm_spzt f
								where a.glbh = b.glbh and f.spzt = a.spzt and c.glbh = b.glbh and b.spbh='' and a.scbh = '00143'

--- 陈列面
SELECT c.hjbh,c.clm,c.dg,c.zs,
									(select nvl2(wh.glbh,'S','') from wh_spz_d wh where wh.glbh=c.glbh and wh.lsh=-81)||lpad(u.dlm,2,'0')||u.zlm||'-'||substr(c.hjbh,-2)||c.ch||c.wz||'-'||c.clm||c.dg||c.zs hjm
									FROM user_xt_spda u,hj_sc a, hj_bh b,hj_sp c
									where a.sclx = b.sclx
										and b.sclx = c.sclx
										and a.hjflm = b.hjflm
										and b.hjbh = c.hjbh
										and a.scbh = '00143'
										and u.glbh = c.glbh
										and u.glbh = '1027094'

--- 库存
select kcsl,to_char(zjdh,'yyyy-mm-dd') ZJDH,to_char(dhrq,'yyyy-mm-dd') DHRQ,pszt,xssl,ddrsl,drxssl from xt_kcb where glbh = '1027094' and scbh = '00143'

--- 库存表在途
--- 193.0.0.5/sjjk', 'sj', 'sjjk'
SELECT ztsl FROM all_xt_ztkc  WHERE scbh = '00143' AND glbh = '1027094';
SELECT SUM (cksl) ZTSL FROM all_rz_zzck
							WHERE NVL (sczt, 'PS') = 'PS'
							AND (scbh IN ('99977', '99979', '99997', '99999')  OR scbh IN (SELECT bmbh FROM dm_bmbh WHERE bmlx = '44') )
							AND glbh = '1027094'
							AND ckrq >= SYSDATE - 5

---订货卡信息
select m.kh, to_char(m.scsj,'yyyy-mm-dd hh24:mi:ss') SCSJ, d.yhsl, m.zkr, m.scr
								from xt_yhk_m m, xt_yhk_d d
								where m.scbh = '00143'
								and d.scbh = m.scbh
								and m.kh = d.kh
								and d.glbh = '1027094'
								and d.sfysc = 'Y'
								and m.scsj >= sysdate - 7
								order by m.kh desc;

---促销活动
select ppno,ppgname,to_char(ppstartdate,'yyyy-mm-dd') PPSTARTDATE,to_char(ppenddate,'yyyy-mm-dd') PPENDDATE from pos.popall_m where ppno in (select ppdno from pos.popall_d where ppdgdid = '$oglbh') and ppenddate >=sysdate -1  and ppmktno = '$scbh';

--- 特卖信息
SELECT tmnr || to_char(c.ZXRQ, 'MM/DD') || '—>' || to_char(C.FHRQ,'MM/DD') TM
								FROM(SELECT ZXRQ,FHRQ,
                              (case when trunc(SYSDATE) < ZXRQ then '将上特卖 '
                                    when trunc(SYSDATE + 2) >= FHRQ then '将下特卖 '
                                    else '正在特卖 ' end) tmnr
                            FROM RZ_SCTJ
                           WHERE ZXRQ<= TRUNC(SYSDATE + 3)
                             AND FHRQ>= TRUNC(SYSDATE)
                             and scbh = '00143'
                             AND GLBH = '1027094'
                             and CXDM in ('L', 'TM', 'LSTM', 'T')
                             and spsj <> 0 ) C
                       WHERE ROWNUM = 1;

---会员卡号、储值卡 193.0.0.19/hdcrm', 'sj', 'sjjxc'
---iccard
select f.balance,to_char(a.lstupdtime,'yyyy-mm-dd hh24:mi:ss') lstupdtime, rtrim(c.name)||'['||rtrim(c.code)||']' cardtype, d.name stat
								from crmcard a,CRMCARDDESDTL f, crmmember b,crmcardtype c,crmcardstat d
								where a.carrier=b.gid and a.cardnum = f.cardnum(+) and a.cardtype=c.code and a.stat=d.stat and a.cardnum = '';
---会员卡
select f.balance,to_char(a.lstupdtime,'yyyy-mm-dd hh24:mi:ss') lstupdtime,to_char(a.bytime,'yyyy-mm-dd') bytime,b.name||'['||rtrim(b.code)||']' carrier, rtrim(c.name)||'['||rtrim(c.code)||']' cardtype, d.name stat
								from crmcard a,CRMCARDDESDTL f, crmmember b,crmcardtype c,crmcardstat d
								where a.carrier=b.gid and a.cardnum = f.cardnum(+) and a.cardtype=c.code and a.stat=d.stat and a.cardnum = ''

--- 查询单个商品销售
SELECT to_char(a.xsrq,'hh24:mi') XSRQ, b.spmc, a.xssl,
					CASE
					WHEN (b.fzm = 10 OR b.fzm = 16) AND b.dlm = 0 THEN
					a.xssl * b.jzsj
					ELSE
					a.xsje
					END AS xsje, a.skjbh, a.czybh, a.lsh
					FROM drspls a, user_xt_spda b
					WHERE a.glbh = b.glbh
					AND (a.jhglbh = '1027094' OR a.glbh = '1027094')
					ORDER BY a.xsrq DESC

--- 查询历史销售
SELECT   to_char(xsrq,'yyyy-mm-dd') XSRQ, ROUND (AVG (NVL (spsj, 0)), 2) spsj,
                             SUM (NVL (xssl, 0)) xssl, SUM (NVL (xsje, 0)) xsje
                     FROM rz_dpxs
                     WHERE glbh = '1027094' AND xsrq >=  to_date('$date1','yyyy-mm-dd') AND xsrq <= to_date('$date2','yyyy-mm-dd')
                     GROUP BY glbh, xsrq
                     ORDER BY xsrq DESC

pkg_cx_rf.p_zlcx(as_report varchar2, as_scbh char, as_glbh char, cur_result out ref_cur)

--- 1.根据条码后六位查询【多个商品】
    when 'tm6cx' then
      open cur_result for
        select a.glbh,b.spmc,a.spbh from xt_ftm a,xt_spda b where a.spbh like '%'||as_glbh and a.glbh = b.glbh;
  --- 2. 根据管理编码查询商品明细[生鲜]
    when 'sxsp' then
      open cur_result for
        select b.spbh,a.dhzt,a.dhfs,a.shfs,f.spztmc,b.glbh,b.scbh,e.spcd,b.plu,b.spmc,b.spsj jzsj,b.hysj jzhyj,b.plulx
            from xt_lwxx a,xt_plu b,xt_ftm e,dm_spzt f
            where a.glbh = b.glbh and f.spzt = a.spzt and e.glbh = b.glbh and b.glbh = as_glbh and b.scbh = as_scbh;

  -- 3.商品称重、金额【生鲜】
    when 'czsp' then
      open cur_result for
        select a.mglbh,b.spmc from xt_zmm a,xt_spda b where a.zglbh = as_glbh and a.scbh = as_scbh and b.glbh = a.mglbh;

--- 4. 根据条码查询商品明细[非生鲜]
    when 'qtmcx' then
      open cur_result for
      select b.spbh,a.dhzt,a.dhfs,a.shfs,f.spztmc,a.glbh,a.scbh,b.spcd,c.spmc,a.jzsj,a.jzhyj
                from xt_lwxx a,xt_ftm b,xt_spda c,dm_spzt f
                where a.glbh = b.glbh and f.spzt = a.spzt and c.glbh = b.glbh and b.spbh=as_glbh and a.scbh = as_scbh;
--- 5. 陈列面
    when 'clm' then
      open cur_result for
        SELECT c.hjbh,c.clm,c.dg,c.zs,
                  (select nvl2(wh.glbh,'S','') from wh_spz_d wh where wh.glbh=c.glbh and wh.lsh=-81)||lpad(u.dlm,2,'0')||u.zlm||'-'||substr(c.hjbh,-2)||c.ch||c.wz||'-'||c.clm||c.dg||c.zs hjm
                  FROM user_xt_spda u,hj_sc a, hj_bh b,hj_sp c
                  where a.sclx = b.sclx
                    and b.sclx = c.sclx
                    and a.hjflm = b.hjflm
                    and b.hjbh = c.hjbh
                    and a.scbh = as_scbh
                    and u.glbh = c.glbh
                    and u.glbh = as_glbh;

--- 6. 库存
    when 'kcb' then
      open cur_result for
        select kcsl,to_char(zjdh,'yyyy-mm-dd') ZJDH,to_char(dhrq,'yyyy-mm-dd') DHRQ,pszt,xssl,ddrsl,drxssl from xt_kcb where glbh = as_glbh and scbh = as_scbh;
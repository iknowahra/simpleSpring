package com.spring.loginMvc.dao;

import com.spring.global.helper.ParamMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName : UserInfoMapper.java
 * @Description :  로그인 성공시 필요한 정보를 조회
 * @author iknowahra
 * @since 2024. 09. 18.
 * @version 1.0
 * @see
 * @Modification Information
 *
 *               <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2024. 09. 18.     iknowahra     	최초 생성
 *               </pre>
 */
@Mapper
public interface UserInfoMapper {

    List<ParamMap> selectMenuListByUser(Map<String, Object> paramMap);

    List<ParamMap> searchCodeList(Map<String, Object> paramMap);

    List<ParamMap> searchCodeOneList(Map<String, Object> paramDTO);

    List<ParamMap> selectUserFavMenu(Map<String, Object> paramMap);

    void mergePslzM(Map<String, Object> paramMap);

    int selectPKMenuFreq(Map<String, Object> paramMap);

    void insertMenuFreq(Map<String, Object> paramMap);

    List<ParamMap> selectNotiBoard(Map<String, Object> paramMap);

    List<ParamMap> selectLangCd();

    List<ParamMap> selectMsgByLangCd(String langCd);

    void insertTxErrLog(Map<String, Object> paramMap);

    void deletePslzM(Map<String, Object> paramMap);

}

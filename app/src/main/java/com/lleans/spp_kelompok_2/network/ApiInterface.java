package com.lleans.spp_kelompok_2.network;

import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.AuthData;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.domain.model.tunggakan.TunggakanData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiInterface {

    // -- Ping Server
    @GET("ping")
    Call<BaseResponse> pingServer();

    // -- Auth
    @FormUrlEncoded
    @POST("v1/auth")
    Call<BaseResponse<AuthData>> postAuthPetugas(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("v1/auth")
    Call<BaseResponse<AuthData>> postAuthSiswa(
            @Field("nisn") String nisn,
            @Field("password") String password
    );


    // -- Kelas
    @GET("v1/kelas")
    Call<BaseResponse<List<KelasData>>> getKelas(
            @Query("id_kelas") Integer id_kelas,
            @Query("nama_kelas") String nama_kelas,
            @Query("jurusan") String jurusan,
            @Query("angkatan") Long angkatan,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("v1/kelas")
    Call<BaseResponse<List<KelasData>>> keywordKelas(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("v1/kelas")
    Call<BaseResponse<KelasData>> postKelas(
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan,
            @Field("angkatan") Long angkatan
    );

    @FormUrlEncoded
    @PUT("v1/kelas")
    Call<BaseResponse<KelasData>> putKelas(
            @Field("id_kelas") Integer id_kelas,
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan,
            @Field("angkatan") Long angkatan
    );

    @DELETE("v1/kelas")
    Call<BaseResponse<KelasData>> deleteKelas(
            @Query("id_kelas") Integer id_kelas
    );


    // -- Pembayaran
    @GET("v1/pembayaran")
    Call<BaseResponse<List<PembayaranData>>> getPembayaran(
            @Query("id_pembayaran") Integer id_pembayaran,
            @Query("id_petugas") Integer id_petugas,
            @Query("nisn") String nisn,
            @Query("tgl_bayar") String tgl_bayar,
            @Query("bulan_spp") Integer bulan_spp,
            @Query("tahun_spp") Integer tahun_spp,
            @Query("id_spp") Integer id_spp,
            @Query("jumlah_bayar") Integer jumlah_bayar,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("v1/pembayaram")
    Call<BaseResponse<List<PembayaranData>>> keywordPembayaran(
            @Query("keyword") String keyword
    );

    @GET("v1/pembayaran/tunggakan")
    Call<BaseResponse<TunggakanData>> getTunggakan(
            @Query("nisn") String nisn
    );

    @FormUrlEncoded
    @POST("v1/pembayaran")
    Call<BaseResponse<PembayaranData>> postPembayaran(
            @Field("id_petugas") Integer id_petugas,
            @Field("nisn") String nisn,
            @Field("tgl_bayar") String tgl_bayar,
            @Field("bulan_spp") Integer bulan_spp,
            @Field("tahun_spp") Integer tahun_spp,
            @Field("id_spp") Integer id_spp,
            @Field("jumlah_bayar") Long jumlah_bayar
    );

    @FormUrlEncoded
    @PUT("v1/pembayaran")
    Call<BaseResponse<PembayaranData>> putPembayaran(
            @Field("id_pembayaran") Integer id_pembayaran,
            @Field("id_petugas") Integer id_petugas,
            @Field("nisn") String nisn,
            @Field("tgl_bayar") String tgl_bayar,
            @Field("bulan_spp") Integer bulan_spp,
            @Field("tahun_spp") Integer tahun_spp,
            @Field("id_spp") Integer id_spp,
            @Field("jumlah_bayar") Long jumlah_bayar
    );

    @DELETE("v1/pembayaran")
    Call<BaseResponse<PembayaranData>> deletePembayaram(
            @Query("id_pembayaran") Integer id_pembayaran
    );


    // -- Petugas
    @GET("v1/petugas")
    Call<BaseResponse<List<PetugasData>>> getPetugas(
            @Query("id_petugas") Integer id_petugas,
            @Query("username") String username,
            @Query("nama_petugas") String nama_petugas,
            @Query("level") String level,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("v1/petugas")
    Call<BaseResponse<List<PetugasData>>> keywordPetugas(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("v1/petugas")
    Call<BaseResponse<PetugasData>> postPetugas(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @FormUrlEncoded
    @PUT("v1/petugas")
    Call<BaseResponse<PetugasData>> putPetugas(
            @Field("id_petugas") Integer id_petugas,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @DELETE("v1/petugas")
    Call<BaseResponse<PetugasData>> deletePetugas(
            @Query("id_petugas") Integer id_petugas
    );


    // -- Siswa
    @GET("v1/siswa")
    Call<BaseResponse<List<SiswaData>>> getSiswa(
            @Query("nisn") String nisn,
            @Query("nis") String nis,
            @Query("nama") String nama,
            @Query("id_kelas") Integer id_kelas,
            @Query("alamat") String alamat,
            @Query("no_telp") String no_telp,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("v1/siswa")
    Call<BaseResponse<List<SiswaData>>> keywordSiswa(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("v1/siswa")
    Call<BaseResponse<SiswaData>> postSiswa(
            @Field("nisn") String nisn,
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("id_kelas") Integer id_kelas,
            @Field("alamat") String alamat,
            @Field("no_telp") String no_telp
    );

    @FormUrlEncoded
    @PUT("v1/siswa")
    Call<BaseResponse<SiswaData>> putSiswa(
            @Field("nisn") String nisn,
            @Field("new_nisn") String new_nisn,
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("id_kelas") Integer id_kelas,
            @Field("alamat") String alamat,
            @Field("no_telp") String no_telp
    );

    @DELETE("v1/siswa")
    Call<BaseResponse<SiswaData>> deleteSiswa(
            @Query("nisn") String nisn
    );


    // -- Spp
    @GET("v1/spp")
    Call<BaseResponse<List<SppData>>> getSpp(
            @Query("id_spp") Integer id_spp,
            @Query("angkatan") Long angkatan,
            @Query("tahun") Long tahun,
            @Query("nominal") Long nominal,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("v1/spp")
    Call<BaseResponse<List<SppData>>> keywordSpp(
            @Query("keyword") String keyword
    );

    @GET("v1/spp/latest")
    Call<BaseResponse<SppData>> getLatestSpp(
            @Query("nisn") String nisn,
            @Query("year") String year
    );

    @FormUrlEncoded
    @POST("v1/spp")
    Call<BaseResponse<SppData>> postSpp(
            @Field("angkatan") Long angkatan,
            @Field("tahun") Long tahun,
            @Field("nominal") Long nominal
    );

    @FormUrlEncoded
    @PUT("v1/spp")
    Call<BaseResponse<SppData>> putSpp(
            @Field("id_spp") Integer id_spp,
            @Field("angkatan") Long angkatan,
            @Field("tahun") Long tahun,
            @Field("nominal") Long nominal
    );

    @DELETE("v1/spp")
    Call<BaseResponse<SppData>> deleteSpp(
            @Query("id_spp") Integer id_spp
    );
}

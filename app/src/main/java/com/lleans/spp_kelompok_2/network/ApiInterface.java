package com.lleans.spp_kelompok_2.network;

import com.lleans.spp_kelompok_2.domain.model.auth.AuthData;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasDataList;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasDataList;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaDataList;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppDataList;
import com.lleans.spp_kelompok_2.domain.model.tunggakan.TunggakanData;;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiInterface {


    //    AUTH
    @FormUrlEncoded
    @POST("auth")
    Call<AuthData> postAuthPetugas(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth")
    Call<AuthData> postAuthSiswa(
            @Field("nisn") String nisn,
            @Field("password") String password
    );


    //    KELAS
    @GET("kelas")
    Call<KelasDataList> getKelas(
            @Header("Authorization") String token,
            @Query("id_kelas") Integer id_kelas,
            @Query("nama_kelas") String nama_kelas,
            @Query("jurusan") String jurusan,
            @Query("angkatan") Integer angkatan,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("kelas")
    Call<KelasDataList> keywordKelas(
            @Header("Authorization") String token,
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("kelas")
    Call<KelasData> postKelas(
            @Header("Authorization") String token,
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan,
            @Field("angkatan") Integer angkatan
    );

    @FormUrlEncoded
    @PUT("kelas")
    Call<KelasData> putKelas(
            @Header("Authorization") String token,
            @Field("id_kelas") Integer id_kelas,
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan,
            @Field("angkatan") Integer angkatan
    );

    @DELETE("kelas")
    Call<KelasData> deleteKelas(
            @Header("Authorization") String token,
            @Query("id_kelas") Integer id_kelas
    );


    //    PEMBAYARAN
    @GET("pembayaran")
    Call<PembayaranDataList> getPembayaran(
            @Header("Authorization") String token,
            @Query("id_pembayaran") Integer id_pembayaran,
            @Query("id_petugas") Integer id_petugas,
            @Query("nisn") String nisn,
            @Query("tgl_dibayar") String tgl_dibayar,
            @Query("bulan_spp") Integer bulan_spp,
            @Query("tahun_spp") Integer tahun_spp,
            @Query("id_spp") Integer id_spp,
            @Query("jumlah_bayar") Integer jumlah_bayar,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("pembayaram")
    Call<PembayaranDataList> keywordPembayaran(
            @Header("Authorization") String token,
            @Query("keyword") String keyword
    );

    @GET("pembayaran/tunggakan")
    Call<TunggakanData> getTunggakan(
            @Header("Authorization") String token,
            @Query("nisn") String nisn
    );

    @FormUrlEncoded
    @POST("pembayaran")
    Call<PembayaranData> postPembayaran(
            @Header("Authorization") String token,
            @Field("id_petugas") Integer id_petugas,
            @Field("nisn") String nisn,
            @Field("tgl_dibayar") String tgl_dibayar,
            @Query("bulan_spp") Integer bulan_spp,
            @Query("tahun_spp") Integer tahun_spp,
            @Field("id_spp") Integer id_spp,
            @Field("jumlah_bayar") Integer jumlah_bayar
    );

    @FormUrlEncoded
    @PUT("pembayaran")
    Call<PembayaranData> putPembayaran(
            @Header("Authorization") String token,
            @Field("id_pembayaran") Integer id_pembayaran,
            @Field("id_petugas") Integer id_petugas,
            @Field("nisn") String nisn,
            @Field("tgl_dibayar") String tgl_dibayar,
            @Query("bulan_spp") Integer bulan_spp,
            @Query("tahun_spp") Integer tahun_spp,
            @Field("id_spp") Integer id_spp,
            @Field("jumlah_bayar") Integer jumlah_bayar
    );

    @DELETE("pembayaran")
    Call<PembayaranData> deletePembayaram(
            @Header("Authorization") String token,
            @Query("id_pembayaran") Integer id_kelas
    );


    //    PETUGAS
    @GET("petugas")
    Call<PetugasDataList> getPetugas(
            @Header("Authorization") String token,
            @Query("id_petugas") Integer id_petugas,
            @Query("username") String username,
            @Query("nama_petugas") String nama_petugas,
            @Query("level") String level,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("petugas")
    Call<PetugasDataList> keywordPetugas(
            @Header("Authorization") String token,
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("petugas")
    Call<PetugasData> postPetugas(
            @Header("Authorization") String token,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @FormUrlEncoded
    @PUT("petugas")
    Call<PetugasData> putPetugas(
            @Header("Authorization") String token,
            @Field("id_petugas") Integer id_petugas,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @DELETE("petugas")
    Call<PetugasData> deletePetugas(
            @Header("Authorization") String token,
            @Query("id_petugas") Integer id_petugas
    );


    //    SISWA
    @GET("siswa")
    Call<SiswaDataList> getSiswa(
            @Header("Authorization") String token,
            @Query("nisn") String nisn,
            @Query("nis") String nis,
            @Query("nama") String nama,
            @Query("id_kelas") Integer id_kelas,
            @Query("alamat") String alamat,
            @Query("no_telp") String no_telp,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("keyword")
    Call<SiswaDataList> keywordSiswa(
            @Header("Authorization") String token,
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("siswa")
    Call<SiswaData> postSiswa(
            @Header("Authorization") String token,
            @Field("nisn") String nisn,
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("id_kelas") Integer id_kelas,
            @Field("alamat") String alamat,
            @Field("no_telp") String no_telp
    );

    @FormUrlEncoded
    @PUT("siswa")
    Call<SiswaData> putSiswa(
            @Header("Authorization") String token,
            @Field("nisn") String nisn,
            @Field("new_nisn") String new_nisn,
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("id_kelas") Integer id_kelas,
            @Field("alamat") String alamat,
            @Field("no_telp") String no_telp
    );

    @DELETE("siswa")
    Call<SiswaData> deleteSiswa(
            @Header("Authorization") String token,
            @Query("nisn") String nisn
    );


    //    SPP
    @GET("spp")
    Call<SppDataList> getSpp(
            @Header("Authorization") String token,
            @Query("id_spp") Integer id_spp,
            @Query("angkatan") Integer angkatan,
            @Query("tahun") Integer tahun,
            @Query("nominal") Integer nominal,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("spp")
    Call<SppDataList> keywordSpp(
            @Header("Authorization") String token,
            @Query("keyword") String keyword
    );

    @GET("spp/latest")
    Call<SppData> getLatestSpp(
            @Header("Authorization") String token,
            @Query("nisn") String nisn,
            @Query("year") String year
    );

    @FormUrlEncoded
    @POST("spp")
    Call<SppData> postSpp(
            @Header("Authorization") String token,
            @Field("angkatan") Integer angkatan,
            @Field("tahun") Integer tahun,
            @Field("nominal") Integer nominal
    );

    @FormUrlEncoded
    @PUT("spp")
    Call<SppData> putSpp(
            @Header("Authorization") String token,
            @Field("id_spp") Integer id_spp,
            @Field("angkatan") Integer angkatan,
            @Field("tahun") Integer tahun,
            @Field("nominal") Integer nominal
    );

    @DELETE("spp")
    Call<SppData> deleteSpp(
            @Header("Authorization") String token,
            @Query("id_spp") Integer id_spp
    );
}

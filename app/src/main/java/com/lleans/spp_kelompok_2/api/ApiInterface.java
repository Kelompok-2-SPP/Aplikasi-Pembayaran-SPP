package com.lleans.spp_kelompok_2.api;

import com.lleans.spp_kelompok_2.model.auth.AuthData;
import com.lleans.spp_kelompok_2.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.model.spp.SppData;

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


    //    AUTH
    @FormUrlEncoded
    @POST("auth")
    Call<AuthData> postAuth(
            @Field("username") String username,
            @Field("password") String password
    );


    //    KELAS
    @GET("kelas")
    Call<KelasData> getKelas(
            @Query("id_kelas") int id_kelas,
            @Query("nama_kelas") String nama_kelas,
            @Query("jurusan") String jurusan,
            @Query("angkatan") int angkatan,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("kelas")
    Call<KelasData> keywordKelas(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("kelas")
    Call<KelasData> postKelas(
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan,
            @Field("angkatan") Integer angkatan
    );

    @FormUrlEncoded
    @PUT("kelas/{id_kelas}")
    Call<KelasData> putKelas(
            @Field("id_kelas") int id_kelas,
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan,
            @Field("angkatan") Integer angkatan
    );

    @DELETE("kelas")
    Call<KelasData> deleteKelas(
            @Query("id_kelas") int id_kelas
    );


    //    PEMBAYARAN
    @GET("pembayaran")
    Call<PembayaranData> getPembayaran(
            @Query("id_pembayaran") int id_pembayaran,
            @Query("id_petugas") int id_petugas,
            @Query("nisn") String nisn,
            @Query("tgl_dibayar") String tgl_dibayar,
            @Query("bulan_dibayar") int bulan_dibayar,
            @Query("tahun_dibayar") int tahun_dibayar,
            @Query("id_spp") int id_spp,
            @Query("jumlah_bayar") int jumlah_bayar,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("pembayaram")
    Call<PembayaranData> keywordPembayaran(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("pembayaran")
    Call<PembayaranData> postPembayaran(
            @Field("id_petugas") int id_kelas,
            @Field("nisn") String nisn,
            @Field("tgl_dibayar") String tgl_dibayar,
            @Field("bulan_dibayar") int bulan_dibayar,
            @Field("tahun_dibayar") int tahun_dibayar,
            @Field("id_spp") int id_spp,
            @Field("jumlah_bayar") int jumlah_bayar
    );

    @FormUrlEncoded
    @PUT("pembayaran")
    Call<PembayaranData> putPembayaran(
            @Field("id_pembayaran") int id_pemabayaran,
            @Field("id_petugas") int id_kelas,
            @Field("nisn") String nisn,
            @Field("tgl_dibayar") String tgl_dibayar,
            @Field("bulan_dibayar") int bulan_dibayar,
            @Field("tahun_dibayar") int tahun_dibayar,
            @Field("id_spp") int id_spp,
            @Field("jumlah_bayar") int jumlah_bayar
    );

    @DELETE("pembayaran")
    Call<PembayaranData> deletePembayaram(
            @Query("id_pembayaran") int id_kelas
    );


    //    PETUGAS
    @GET("petugas")
    Call<PetugasData> getPetugas(
            @Query("id_petugas") int id_petugas,
            @Query("username") String username,
            @Query("nama_petugas") String nama_petugas,
            @Query("level") String level,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("petugas")
    Call<PetugasData> keywordPetugas(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("petugas")
    Call<PetugasData> postPetugas(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @FormUrlEncoded
    @PUT("petugas")
    Call<PetugasData> putPetugas(
            @Field("id_petugas") int id_petugas,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @DELETE("petugas")
    Call<PetugasData> deletePetugas(
            @Query("id_petugas") int id_petugas
    );


    //    SISWA
    @GET("siswa")
    Call<SiswaData> getSiswa(
            @Query("nisn") String nisn,
            @Query("nis") String nis,
            @Query("nama") String nama,
            @Query("id_kelas") int id_kelas,
            @Query("alamat") String alamat,
            @Query("no_telp") String no_telp,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("keyword")
    Call<SiswaData> keywordSiswa(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("siswa")
    Call<SiswaData> postSiswa(
            @Field("nisn") String nisn,
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("id_kelas") int id_kelas,
            @Field("alamat") String alamat,
            @Field("no_telp") String no_telp
    );

    @FormUrlEncoded
    @PUT("siswa")
    Call<SiswaData> putSiswa(
            @Field("nisn") String nisn,
            @Field("new_nisn") String new_nisn,
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("id_kelas") int id_kelas,
            @Field("alamat") String alamat,
            @Field("no_telp") String no_telp
    );

    @DELETE("siswa")
    Call<SiswaData> deleteSiswa(
            @Query("nisn") String nisn
    );


    //    SPP
    @GET("spp")
    Call<SppData> getSpp(
            @Query("id_spp") int id_spp,
            @Query("angkatan") int angkatan,
            @Query("tahun") int tahun,
            @Query("nominal") int nominal,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("spp")
    Call<SppData> keywordSpp(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("spp")
    Call<SiswaData> postSpp(
            @Field("angkatan") int angkatan,
            @Field("tahun") int tahun,
            @Field("naominal") int nominal
    );

    @FormUrlEncoded
    @PUT("spp")
    Call<SiswaData> putSpp(
            @Field("id_spp") int id_spp,
            @Field("angkatan") int angkatan,
            @Field("tahun") int tahun,
            @Field("naominal") int nominal
    );

    @FormUrlEncoded
    @DELETE("spp")
    Call<SppData> deleteSpp(
            @Field("id_spp") int id_spp
    );
}

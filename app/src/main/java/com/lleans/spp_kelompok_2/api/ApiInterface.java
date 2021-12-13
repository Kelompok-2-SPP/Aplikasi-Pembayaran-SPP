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
    Call<List<KelasData>> getKelas(
            @Query("id_kelas") int id_kelas,
            @Query("nama_kelas") String nama_kelas,
            @Query("jurusan") String jurusan,
            @Query("angkatan") int angkatan,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("kelas")
    Call<List<KelasData>> keywordKelas(
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
    Call<List<KelasData>> deleteKelas(
            @Query("id_kelas") int id_kelas
    );


    //    PEMBAYARAN
    @GET("pembayaran")
    Call<List<PembayaranData>> getPembayaran(
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
    Call<List<PembayaranData>> keywordPembayaran(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("pembayaran")
    Call<List<PembayaranData>> postPembayaran(
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
    Call<List<PembayaranData>> putPembayaran(
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
    Call<List<PembayaranData>> deletePembayaram(
            @Query("id_pembayaran") int id_kelas
    );


    //    PETUGAS
    @GET("petugas")
    Call<List<PetugasData>> getPetugas(
            @Query("id_petugas") int id_petugas,
            @Query("username") String username,
            @Query("nama_petugas") String nama_petugas,
            @Query("level") String level,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("petugas")
    Call<List<PetugasData>> keywordPetugas(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("petugas")
    Call<List<PetugasData>> postPetugas(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @FormUrlEncoded
    @PUT("petugas")
    Call<List<PetugasData>> putPetugas(
            @Field("id_petugas") int id_petugas,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level
    );

    @DELETE("petugas")
    Call<List<PetugasData>> deletePetugas(
            @Query("id_petugas") int id_petugas
    );


    //    SISWA
    @GET("siswa")
    Call<List<SiswaData>> getSiswa(
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
    Call<List<SiswaData>> keywordSiswa(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("siswa")
    Call<List<SiswaData>> postSiswa(
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
    Call<List<SiswaData>> putSiswa(
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
    Call<List<SiswaData>> deleteSiswa(
            @Query("nisn") String nisn
    );


    //    SPP
    @GET("spp")
    Call<List<SppData>> getSpp(
            @Query("id_spp") int id_spp,
            @Query("angkatan") int angkatan,
            @Query("tahun") int tahun,
            @Query("nominal") int nominal,
            @Query("createdAt") String createdAt,
            @Query("updatedAt") String updatedAt
    );

    @GET("spp")
    Call<List<SppData>> keywordSpp(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("spp")
    Call<List<SiswaData>> postSpp(
            @Field("angkatan") int angkatan,
            @Field("tahun") int tahun,
            @Field("naominal") int nominal
    );

    @FormUrlEncoded
    @PUT("spp")
    Call<List<SiswaData>> putSpp(
            @Field("id_spp") int id_spp,
            @Field("angkatan") int angkatan,
            @Field("tahun") int tahun,
            @Field("naominal") int nominal
    );

    @FormUrlEncoded
    @DELETE("spp")
    Call<List<SppData>> deleteSpp(
            @Field("id_spp") int id_spp
    );
}

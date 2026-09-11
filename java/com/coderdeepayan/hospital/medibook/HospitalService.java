package com.coderdeepayan.hospital.medibook;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@SuppressLint({"NewApi", "LocalSuppress"})

public class HospitalService {

        private static final String SERVER_TIME_URL =
                "https://hmis.rcil.gov.in/HISServices/service/railtelService/server-time";

        private static final String QR_STAMPING_URL =
                "https://hmis.rcil.gov.in/HISServices/service/mobile-service/callStampingService";

        // HMAC secret
        private static final String HMAC_SECRET =
                "iUreG2oFXJfB+4ufJ02yzD6Pt3DG16idwo2wWrqUptujbrXHaKtdxzO9KHuHbMFv9vCAm4nNi7ZJX6svWxwlVQ==";

        // AES Key
        private static final String AES_KEY = "x7v!A%C*F-JaNdRgUjXn2r5u8x/A?D(G";

        // AES IV
        private static final String AES_IV = "6v9y$B&E)H@McQfT";

    private List<DiagnosticResult> getSmartReportDetails(String crNo,String hospitalCode,String sampleNo,
                                                         String isSampleEmpty)
            throws Exception {
        List<DiagnosticResult> diagnosticResultList = new ArrayList<>();
        String url = "https://hmis.rcil.gov.in/HISDRDESK/services/restful/" +
                "mobile-service/sampleWiseDataDetailsMobile?" +
                "crNo="+URLEncoder.encode(encrypt(crNo), StandardCharsets.UTF_8)+
                "&hospCode="+URLEncoder.encode(encrypt(hospitalCode), StandardCharsets.UTF_8)+
                "&sampleno="+URLEncoder.encode(encrypt(sampleNo), StandardCharsets.UTF_8)+
                "&isSampleNoEmpty="+URLEncoder.encode(encrypt(isSampleEmpty), StandardCharsets.UTF_8);
        String serverTime = getServerTime();
        String bearer = createBearer(serverTime);

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearer);
        con.setRequestProperty("X-App-Encrypted", "true");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();
        JSONArray jsonArray = new JSONArray(sb.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            String actual = jsonArray.getJSONObject(i).getString("value").replaceAll("-","").trim();
            if (actual.length()>0){
                diagnosticResultList.add(new DiagnosticResult(
                        jsonArray.getJSONObject(i).getString("parametername"), actual,
                        jsonArray.getJSONObject(i).getString("standardrange"),
                        jsonArray.getJSONObject(i).getString("isOutOfRange").equals("0")
                ));
            }
        }

        return diagnosticResultList;
    }

    public String getSmartReport(String crNo) throws Exception {
        String url = "https://hmis.rcil.gov.in/HISDRDESK/services/restful/mobile-service/" +
                "sampleWiseDataListMobile?crNo="+URLEncoder.encode(encrypt(crNo), StandardCharsets.UTF_8)+
                "&hosCode="+URLEncoder.encode(encrypt("\\"), StandardCharsets.UTF_8);
        String serverTime = getServerTime();
        String bearer = createBearer(serverTime);

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearer);
        con.setRequestProperty("X-App-Encrypted", "true");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        br.close();
        return sb.toString();
    }

    public void getLabReportFile(Diagnosis diagnosis) throws Exception {
        String url = "https://hmis.rcil.gov.in/HISServices/service/mobile-service/reportData?" +
                "crNo=" + URLEncoder.encode(encrypt(diagnosis.getCrNo()), StandardCharsets.UTF_8)+
                "&reqDNo="+URLEncoder.encode(encrypt(diagnosis.getRequestNo()), StandardCharsets.UTF_8)+
                "&hosCode="+URLEncoder.encode(encrypt(diagnosis.getHospitalCode()), StandardCharsets.UTF_8);
        String serverTime = getServerTime();
        String bearer = createBearer(serverTime);

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearer);
        con.setRequestProperty("X-App-Encrypted", "true");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        br.close();

        String data = new JSONArray(sb.toString()).getJSONObject(0).getString("PDFDATA");
        String filename = "LAB_"+diagnosis.getPatientName()+"_"+diagnosis.getTestName()+"_"+
                diagnosis.getRequestDate()+".pdf";
        filename = filename.replaceAll("/","_");
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        FileOutputStream fileOutputStream = new FileOutputStream(downloadsFolder.getAbsolutePath()+"/"+filename);
        fileOutputStream.write(Base64.getDecoder().decode(data));
        fileOutputStream.flush();
        fileOutputStream.close();

    }

    public List<Diagnosis> getLabTestByDoctor(String crNo,String name) throws Exception {
        List<Diagnosis> diagnosisList = new ArrayList<>();
        String url = "https://hmis.rcil.gov.in/HISServices/service/mobile-service/getInvDetails?" +
                "hospCode=" + URLEncoder.encode(encrypt("100"), StandardCharsets.UTF_8) +
                "&crno=" +URLEncoder.encode(encrypt(crNo), StandardCharsets.UTF_8);
        String serverTime = getServerTime();
        String bearer = createBearer(serverTime);

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearer);
        con.setRequestProperty("X-App-Encrypted", "true");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        br.close();
        JSONArray  jsonArray= new JSONObject (sb.toString()).getJSONArray("INVESTIGATION_DETAILS");
        for (int i = 0; i <jsonArray.length(); i++) {
            String[] temp = jsonArray.getJSONObject(i).getString("SAMPLENO_LABNO").split("/");
            List<DiagnosticResult> diagnosticResultList = null;
            Log.d("Lab", "getLabTestByDoctor: "+ Arrays.toString(temp));

            if (temp.length>0){
                if (temp[0].trim().length()>0){
                    diagnosticResultList = getSmartReportDetails(crNo,
                            jsonArray.getJSONObject(i).getString("HOSPITALCODE"),
                            temp[0].trim(),temp[0].trim());
                }
            }

            diagnosisList.add(new Diagnosis(jsonArray.getJSONObject(i).getString("TESTNAME"),name,
                    crNo,jsonArray.getJSONObject(i).getString("HOSPITALCODE"),
                    jsonArray.getJSONObject(i).getString("DEPT_NAME"),
                    jsonArray.getJSONObject(i).getString("LABNAME"),
                    jsonArray.getJSONObject(i).getString("HIVTNUM_REQ_DNO"),
                            jsonArray.getJSONObject(i).getString("REQDATE"),
                    jsonArray.getJSONObject(i).getString("STATUS").toLowerCase().contains("generated"),
                    diagnosticResultList));
        }
        return diagnosisList;
    }
    public void getPatientPrescriptionFile(Prescription prescription) throws Exception {

            String url = "https://hmis.rcil.gov.in/HISDRDESK/services/restful/mobile-service/digi?" +
                    "hosp_code=" + URLEncoder.encode(encrypt(prescription.getHospitalCode()), StandardCharsets.UTF_8) +
                    "&Modval=" + URLEncoder.encode(encrypt("5"), StandardCharsets.UTF_8) +
                    "&CrNo=" +URLEncoder.encode(encrypt(prescription.getCrno()), StandardCharsets.UTF_8)+
                    "&episodeCode=" +URLEncoder.encode(encrypt(prescription.getEpisodeCode()), StandardCharsets.UTF_8)+
                    "&visitNo=" +URLEncoder.encode(encrypt(prescription.getVisitNo()), StandardCharsets.UTF_8)+
                    "&seatId=" + URLEncoder.encode(encrypt("0"), StandardCharsets.UTF_8)+
                    "&Entrydate=" + URLEncoder.encode(encrypt(prescription.getEntryDate()), StandardCharsets.UTF_8);

            String serverTime = getServerTime();
            String bearer = createBearer(serverTime);

            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + bearer);
            con.setRequestProperty("X-App-Encrypted", "true");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);

            br.close();
            String filename = prescription.getPatient_name()+"_"+prescription.getDepartmentUnitName()+"_"+
                    prescription.getFormattedDate().replaceAll("/","-")+".pdf";
            filename = filename.replaceAll("/","_");

            File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            FileOutputStream fileOutputStream = new FileOutputStream(downloadsFolder.getAbsolutePath()+"/"+filename);

            fileOutputStream.write(Base64.getDecoder().decode(sb.toString()));
            fileOutputStream.flush();
            fileOutputStream.close();
    }
    public String getBookingData(String patientCRno) throws Exception {
        String serverTime = getServerTime();
        String bearer = createBearer(serverTime);

        String url = "https://hmis.rcil.gov.in/HISServices/service/mobile-service/getpatEpisodeDtls/2?"+
                "patCrNo="+URLEncoder.encode(encrypt(patientCRno), StandardCharsets.UTF_8)+
                "&hospCode="+URLEncoder.encode(encrypt("0"), StandardCharsets.UTF_8);
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearer);
        con.setRequestProperty("X-App-Encrypted", "true");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        br.close();
        return sb.toString();
    }

    public String getLiveQueueStatus(String patientCRno) throws Exception{

        HttpURLConnection con = (HttpURLConnection) new URL(
                "https://hmis.rcil.gov.in/HISServices/service/" +
                        "genericAppointment/getQNoStatus/3?hospCode=0&patCrNo="+patientCRno).openConnection();
        con.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        br.close();
        return sb.toString();
    }

    public List<Prescription> getPatientVisitData(Patient patient) throws Exception {
        List<Prescription> prescriptionList = new ArrayList<>();
        String serverTime = getServerTime();
        String bearer = createBearer(serverTime);

        String url = "https://hmis.rcil.gov.in/HISServices/service/mobile-service/prescriptionList?"+
                "crno="+URLEncoder.encode(encrypt(patient.getCrNo()), StandardCharsets.UTF_8)+
                "&hosCode="+URLEncoder.encode(encrypt("0"), StandardCharsets.UTF_8);
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearer);
        con.setRequestProperty("X-App-Encrypted", "true");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        br.close();
        JSONArray jsonArray = new JSONObject(sb.toString()).getJSONArray("pat_details");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");

        for (int i = 0; i < jsonArray.length() ; i++) {
            String[] date = jsonArray.getJSONObject(i).getString("ENTRY_DATE").split(" ")[0].split("-");
            LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(date[0]),
                    Integer.parseInt(date[1]),Integer.parseInt(date[2]),0,0);
            prescriptionList.add(new Prescription(
                    jsonArray.getJSONObject(i).getString("GNUM_HOSPITAL_CODE"),
                    jsonArray.getJSONObject(i).getString("HRGNUM_PUK"),
                    jsonArray.getJSONObject(i).getString("HRGNUM_EPISODE_CODE"),
                    jsonArray.getJSONObject(i).getString("HRGNUM_VISIT_NO"),
                    jsonArray.getJSONObject(i).getString("ENTRY_DATE"),
                    jsonArray.getJSONObject(i).getString("GSTR_DEPT_NAME"),
                    jsonArray.getJSONObject(i).getString("HOSP_NAME"),
                    localDateTime.format(dateTimeFormatter),
                    patient.getName()
                    ));
        }
        return prescriptionList;
    }

        public List<Patient> getUMID_data(String phoneNumber) throws Exception {
            URL url = new URL("https://hmis.rcil.gov.in/HISServices/service/railtelService/" +
                    "getUMIDData?mobileNo="+phoneNumber);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();

            List<Patient> patientList = null;
            JSONObject jsonObject = new JSONObject(sb.toString());

            if (jsonObject.getString("status").equalsIgnoreCase("success")){
                patientList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length() ; i++) {
                    patientList.add(new Patient(jsonArray.getJSONObject(i).getString("name"),
                            jsonArray.getJSONObject(i).getString("cr_no"),
                            jsonArray.getJSONObject(i).getString("umid_no")));
                }
            }
            return patientList;
        }

        public List<Hospital> getHospitalList() throws Exception {
            String serverTime = getServerTime();
            String bearer = createBearer(serverTime);

            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://hmis.rcil.gov.in/HISServices/service/mobile-service/getHospitalList?zoneId=" +
                            URLEncoder.encode(encrypt("0"), StandardCharsets.UTF_8) +
                            "&divisionId="
                            +URLEncoder.encode(encrypt("0"), StandardCharsets.UTF_8)).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + bearer);
            con.setRequestProperty("X-App-Encrypted", "true");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();

            JSONArray jsonArray = new JSONObject(sb.toString()).getJSONArray("hospital_details");
            List<Hospital> hospitalList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                hospitalList.add(new Hospital(jsonObject.getString("NAME"),
                        jsonObject.getString("ADDRESS"), jsonObject.getString("CODE"),Hospital.HOSPITAL));
            }

            return hospitalList;
        }

        public List<Department> getDoctorsList(String hospitalCode) throws Exception{
            String serverTime = getServerTime();
            String bearer = createBearer(serverTime);

            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://hmis.rcil.gov.in/HISServices/service/mobile-service/consultantByDept?deptCode="+
                            URLEncoder.encode(encrypt("0"), StandardCharsets.UTF_8)+"&hospCode="
                            +URLEncoder.encode(encrypt(hospitalCode), StandardCharsets.UTF_8)).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + bearer);
            con.setRequestProperty("X-App-Encrypted", "true");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);

            br.close();
            JSONArray jsonArray = new JSONArray(sb.toString());
            List<Department> departmentList = null;
            if (jsonArray.length()>0){
                departmentList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length() ; i++) {
                    departmentList.add(new Department(jsonArray.getJSONObject(i).getString("deptName"),
                            jsonArray.getJSONObject(i).getString("unitName"),
                            jsonArray.getJSONObject(i).getString("opdName"),
                            jsonArray.getJSONObject(i).getString("deptCode"),
                            jsonArray.getJSONObject(i).getString("deptUnitCode")));
                }
            }


            return departmentList;

        }

        public String bookAppointment(Patient patient,Department department,String hospitalCode) throws Exception {

            String bearer = createBearer(getServerTime());
            URL url = new URL(QR_STAMPING_URL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", "Bearer " + bearer);
            con.setRequestProperty("X-App-Encrypted", "true");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            JSONObject appointmentObject = new JSONObject();
            appointmentObject.put("deptcode", department.getDepartmentCode());
            appointmentObject.put("deptunitcode", department.getDeptUnitCode());
            appointmentObject.put("hospitalcode", hospitalCode);
            appointmentObject.put("patcrno", patient.getCrNo());
            appointmentObject.put("iskiosk", "0");

            String body = "jsonData=" + URLEncoder.encode(encrypt(appointmentObject.toString()),
                    StandardCharsets.UTF_8);

            OutputStream os = con.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);

            br.close();
            JSONObject jsonObject = new JSONObject(sb.toString());
            String response = jsonObject.getJSONArray("details").getJSONObject(0)
                                        .getString("MSG").toLowerCase();
            Log.d("Response", "bookAppointment: "+response);

            if (response.contains("successfully")){
                String[] temp = response.split(" ");
                return temp[temp.length-1];
            } else if (response.contains("visited")) {
                return "Booked";
            }
            return null;
        }

        private String getServerTime() throws Exception {

            URL url = new URL(SERVER_TIME_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);

            br.close();
            return sb.toString().trim();
        }

        private String createBearer(String serverTime) throws Exception {
            SecretKeySpec key = new SecretKeySpec(HMAC_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            byte[] hash = mac.doFinal(serverTime.getBytes(StandardCharsets.UTF_8));
            return "RAIL." + serverTime + "." + Base64.getEncoder().encodeToString(hash);
        }

        private String encrypt(String plainText) throws Exception {
            IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        }
    }


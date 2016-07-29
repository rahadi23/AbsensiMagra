<?php


$servername = "localhost";
$username = "root";
$password = "";
$dbname = "db_magradika";
date_default_timezone_set("Asia/Bangkok");


try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    // set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    if(isset($_GET['kode'])){
        $fungsi = $_GET['kode'];

        if($fungsi == "getStatusLogin"){
            $nim    = $_GET['nim'];
            $password = $_GET['password'];

            $query = "SELECT nama FROM panitia WHERE password = '$password' AND nim = '$nim'";
            $stmt = $conn->prepare($query);
            $stmt->execute();

            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $result = $stmt->fetchAll();

            if(count($result)!=0){
                $status_login['respon']=1;
                echo json_encode($status_login);
            }else{
                $status_login['respon'] =0;
                echo json_encode($status_login);
            }
        }else if($fungsi == "getNama"){
            $nim    = $_GET['nim'];
            $password = $_GET['password'];

            $query = "SELECT nama FROM panitia WHERE password = '$password' AND nim = '$nim'";
            $stmt = $conn->prepare($query);
            $stmt->execute();

            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $result = $stmt->fetchAll();
            
            echo json_encode(array('respon'=>$result));
        }else if($fungsi == "uploadAbsensi"){
            $nomor_pendaftaran = $_GET['nomor_pendaftaran'];
            $tanggal = $_GET['tanggal'];
            $jam_datang = $_GET['jam_datang'];
            $time_stamp = date("Y-m-d h:i:sa");
           
            
                    $queryInsert = "INSERT INTO absensi (tanggal,jam_datang,no_pendaftaran,time_stamp) VAlUES ('$tanggal','$jam_datang','$nomor_pendaftaran','$time_stamp')";
                    $conn->exec($queryInsert);
                    $statusInsert['respon']=1;
                    echo json_encode($statusInsert);                
            
        }else if($fungsi == "getListMaba"){
            $nim_pk = $_GET['nim_pk'];

            $queryGetMaba = "SELECT m.no_pendaftaran, m.nama, m.path_foto, m.asal_prop, m.no_hp, m.email FROM mahasiswa m INNER JOIN kelompok k ON m.id_kelompok=k.id_kelompok WHERE k.nim_pk='$nim_pk'";
            $stmt = $conn->prepare($queryGetMaba);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $result = $stmt->fetchAll();

            echo json_encode(array('respon'=>$result));
        }
    }    
}catch(PDOException $e)
    {
    $e->getMessage();
    }

$conn = null;
?> 





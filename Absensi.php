<?php

class Absensi extends CI_Controller {

	function __construct() {
	        // Call the Model constructor
		parent::__construct();
		$this->load->database();
	}

	public function getStatusLogin() {
		// echo $this->uri->total_segments();
		// form_hidden('nim', $this->uri->segment(3));
		// form_hidden('password', $this->uri->segment(4));

		$nim    = $this->uri->segment(3);
		$password = $this->uri->segment(4);

				//ubah query harusnya hanya kakak PK yang bisa login -> sudah
		// $query = "SELECT p.nama FROM akun a INNER JOIN panitia p ON a.id=p.id WHERE passwordMd5 = '$password' AND p.id = '$nim'";
		$this->db->distinct();
		$this->db->select('panitia.nama');
		$this->db->from('akun');
		$this->db->join('panitia', 'akun.id = panitia.id', 'inner');
		$this->db->join('maba_kelompok', 'panitia.id = maba_kelompok.pk', 'inner');
		$this->db->where("passwordMd5 ='$password' AND panitia.id = '$nim' AND maba_kelompok.pk = '$nim'");
		$result = $this->db->get();
		// echo $this->db->get_compiled_select();

		$result=$result->result_array();

		header('Content-Type: application/json');
		if(!empty($result)){
			$_SESSION['result'] = $result;
			$status_login['respon']=1;
			echo json_encode($status_login);
		}else{
			unset($_SESSION['result']);
			$status_login['respon'] =0;
			echo json_encode($status_login);
		}
	}

	public function getNama() {
		$nim    = $this->uri->segment(3);
		$password = $this->uri->segment(4);

		$this->db->distinct();
		$this->db->select('panitia.nama, panitia.id');
		$this->db->from('akun');
		$this->db->join('panitia', 'akun.id = panitia.id', 'inner');
		$this->db->join('maba_kelompok', 'panitia.id = maba_kelompok.pk', 'inner');
		$this->db->where("passwordMd5 ='$password' AND panitia.id = '$nim' AND maba_kelompok.pk = '$nim'");
		$result = $this->db->get();
		// echo $this->db->get_compiled_select();

		$result=$result->result_array();
		header('Content-Type: application/json');
		if(!empty($result)) {
			echo json_encode(array('respon'=>$result));
		} else {
			$status_login['respon'] =0;
			echo json_encode($status_login);
		}
	}

	public function uploadAbsensi() {
		//konsultasikan mengenai keamanannya
				// $nomor_pendaftaran = $_GET['nomor_pendaftaran']; belum diperlukan
		$id = $this->uri->segment(4);
		$tanggal = $this->uri->segment(3);
		$jam_datang = urldecode($this->uri->segment(5));

		header('Content-Type: application/json');
		if(isset($id) && isset($tanggal) && isset($jam_datang)) {

			$base_waktu = "2016-09-05 00:00:00"; //Hari Pertama MP2K
			$base_terlambat = $tanggal." 06:30:00"; //Batas Keterlambatan MP2K
			$base_terlambat_oc = $tanggal." 08:00:00"; //Batas Keterlambatan OC
			$waktu = $tanggal.' '.$jam_datang;

			$harike = floor((strtotime($waktu)-strtotime($base_waktu))/(24*60*60)) + 1;
			$terlambat = (strtotime($waktu)-strtotime($base_terlambat))/(24*60*60);
			$terlambatoc = (strtotime($waktu)-strtotime($base_terlambat_oc))/(24*60*60);

			if(($harike > 0 && $terlambat > 0) || $terlambatoc > 0) {
				$ket = 'T'; //Terlambat
			} else {
				$ket = 'H'; //Hadir
			}

			if(date_parse($waktu)['day'] < 3 && date_parse($waktu)['month'] === 9 && date_parse($waktu)['year'] === 2016) { //Opening Ceremony
				$this->db->set('ketoc', $ket);
				$this->db->set('waktuoc', $jam_datang);
			} elseif($harike > 0) { //MP2K
				$this->db->set('kethari'.$harike,$ket);
				$this->db->set('waktuhari'.$harike,$jam_datang);
			}
			$this->db->where("id='$id'");
			// echo $this->db->get_compiled_update('maba_absensi');

			$this->db->update('maba_absensi');
			$this->db->trans_complete();

			if($this->db->trans_status()) {
				$statusInsert['respon']=1;
			} else {
				$statusInsert['respon']=0;
			}
				//$harike didapat dari penyesuaian dengan substr($tanggal)
				// $time_stamp = date("Y-m-d h:i:sa"); belum diperlukan

				//kesepakatan absensi dibuat memanjang ke kanan
				// $queryInsert = "INSERT INTO absensi (tanggal,jam_datang,no_pendaftaran,time_stamp) VALUES ('$tanggal','$jam_datang','$nomor_pendaftaran','$time_stamp')";
			// $queryInsert = "UPDATE maba_absensi SET kethari".$harike."='H', waktuhari".$harike."='$jam_datang' WHERE id='$id'";
			echo json_encode($statusInsert); 
		} else {
			$status_login['respon'] =0;
			echo json_encode($status_login);
		}
	}

	public function getListMaba() {
		$pk = $this->uri->segment(3);

		$queryGetMaba = "SELECT m.id, m.nama, m.path_foto, m.daerahAsalProv, m.noHp, m.email FROM maba_nimb n, maba_kelompok k, maba m WHERE n.id_kelompok = k.kelompok AND k.pk = '$pk' AND m.id=n.id";
		$this->db->select('m.id, m.nama, m.path_foto, m.daerahAsalProv, m.noHp, m.email, k.namaKelompok');
		$this->db->order_by('k.kelompok');
		$this->db->from('maba_nimb n, maba_kelompok k, maba m');
		$this->db->where("SUBSTRING(n.nimb,1,2) = k.kelompok AND k.pk = '$pk' AND m.id=n.id");
		$result = $this->db->get();
			// echo $this->db->get_compiled_select();

		$result=$result->result_array();
		header('Content-Type: application/json');
		if(!empty($result)) {
			echo json_encode(array('respon'=>$result));
		} else {
			$status_login['respon'] =0;
			echo json_encode($status_login);
		}
		
	}

}

?> 





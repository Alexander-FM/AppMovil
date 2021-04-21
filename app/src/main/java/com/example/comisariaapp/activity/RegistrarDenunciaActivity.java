package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.entity.service.Policia;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.utils.DateDeserializer;
import com.example.comisariaapp.utils.DatePickerFragment;
import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.InformacionAdicional;
import com.example.comisariaapp.entity.service.TipoDenuncia;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.viewmodel.DistritoViewModel;
import com.example.comisariaapp.viewmodel.EstadoCivilViewModel;
import com.example.comisariaapp.viewmodel.InformacionAdicionalViewModel;
import com.example.comisariaapp.viewmodel.TipoDenunciaViewModel;
import com.example.comisariaapp.viewmodel.VinculoParteDenunciadaViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrarDenunciaActivity extends AppCompatActivity {
    private DistritoViewModel distritoViewModel;
    private VinculoParteDenunciadaViewModel vpdViewModel;
    private TipoDenunciaViewModel tdViewModel;
    private InformacionAdicionalViewModel infoAdicViewModel;
    private EstadoCivilViewModel ecViewModel;
    //CON RESPECTO A LOS DATOS DE LA DENUNCIA
    private EditText edtFechaHechos, edtLugarHechos, edtReferenciaHechos;
    private MaterialSpinner drop_distritoD, drop_vpd, drop_td;
    //CON RESPECTO A LOS DATOS DEL AGRAVIADO
    private CheckBox mismaPersona;
    public MaterialSpinner drop_tipoIdentificacionA, drop_generoA, drop_distritoA, drop_infoAdicionalA, drop_medidaProteccion, drop_Juzgado;
    private EditText edtDocA, edtApellidoPaternoA, edtApellidoMaternoA, edtNombresA, edtFechaNacimientoA, edtCeluarA, edtReferenciaDomicilioReal, edtHechoDenunciar,
            edtFechaEmisionProteccion, edtDetalleProtección;

    //CON RESPECTO A LOS DATOS DEL DENUNCIADO
    private MaterialSpinner sp_TipoIdentificacionD, sp_DistritoDenunciado, sp_EstadoCivilD, sp_InfoAdicionalD, sp_GeneroD;

    private EditText edt_DocD, edt_NombresD, edt_ApellidoPaternoD, edt_ApellidoMaternoD, edt_FechaNacimientoD, edt_CelularD, edt_DireccionD, dtp_FechaNacD;

    private Button btnSaveA, btnSaveD;

    private List<Distrito> distritos = new ArrayList<>(), allDistritos = new ArrayList<>();
    private List<VinculoParteDenunciada> vinculos = new ArrayList<>();
    private List<TipoDenuncia> tiposDenuncia = new ArrayList<>();
    private List<InformacionAdicional> infosAdicional = new ArrayList<>();
    private List<EstadoCivil> estadosCiviles = new ArrayList<>();
    private ArrayAdapter<String> adapterDistritos, adapterAllDistritos, adapterVinculos, adapterTd, adapterInfAdic, adapterEstadosCiviles;
    private List<String> displayDistritos = new ArrayList<>(), displayAllDistritos = new ArrayList<>(), displayVinculos = new ArrayList<>(), displayTd = new ArrayList<>(), displayInfAdic = new ArrayList<>(), displayEstadosCiviles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_registrar_denuncia);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });

        ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.vpdViewModel = vmp.get(VinculoParteDenunciadaViewModel.class);
        this.tdViewModel = vmp.get(TipoDenunciaViewModel.class);
        this.infoAdicViewModel = vmp.get(InformacionAdicionalViewModel.class);
        this.ecViewModel = vmp.get(EstadoCivilViewModel.class);
        this.init();
        this.initAdapters();
        this.loadData();
        this.setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudetalle, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.usuariosagregados:
                if (validarDatosDenuncia()) {
                    this.save();
                    this.startActivity(new Intent(this, DetalleDenunciaActivity.class));
                    this.overridePendingTransition(R.anim.above_in, R.anim.above_out);
                    break;
                } else {
                    Toast.makeText(this, "Para continuar debe llenar los datos de la denuncia primero ⛔", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validarDatosDenuncia() {
        return !edtFechaHechos.getText().toString().equals("")
                && drop_distritoD.getSelectedItemPosition() != -1
                && !edtLugarHechos.getText().toString().equals("")
                && !edtReferenciaHechos.getText().toString().equals("")
                && drop_vpd.getSelectedItemPosition() != -1
                && drop_td.getSelectedItemPosition() != -1;
    }

    private void cambiarEstadoCampos(boolean b) {
        drop_tipoIdentificacionA.setEnabled(b);
        edtDocA.setEnabled(b);
        edtNombresA.setEnabled(b);
        edtApellidoPaternoA.setEnabled(b);
        edtApellidoMaternoA.setEnabled(b);
        edtFechaNacimientoA.setEnabled(b);
        edtReferenciaDomicilioReal.setEnabled(b);
        edtCeluarA.setEnabled(b);
        //drop_distritoA.setEnabled(b);
        drop_generoA.setEnabled(b);
        if (b) {
            clearCamposAgraviado();
        } else {
            this.asignarDatosagraviadoSesion();
        }
    }

    private void asignarCamposProteccion(Boolean b) {
        drop_Juzgado.setEnabled(b);
        edtFechaEmisionProteccion.setEnabled(b);
        edtDetalleProtección.setEnabled(b);
        if (b == false) {
            drop_Juzgado.setSelection(0);
            edtDetalleProtección.setText("");
        }
    }

    private void asignarDatosagraviadoSesion() {
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                //.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .create();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String strU = preferences.getString("UsuarioJson", "");
        if (!strU.equals("")) {
            Usuario us = g.fromJson(strU, Usuario.class);
            this.drop_tipoIdentificacionA.setSelection((us.getTipoIdentificacion().getId() == 1 ? 0 : 1) + 1);
            this.edtDocA.setText(us.getNumeroIdentificacion());
            this.edtNombresA.setText(us.getNombres());
            this.edtApellidoPaternoA.setText(us.getApellidoPaterno());
            this.edtApellidoMaternoA.setText(us.getApellidoMaterno());
            String strFechaN;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate ldate = Instant.ofEpochMilli(us.getFechaNacimiento().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                strFechaN = ldate.getDayOfMonth() + "-" + (ldate.getMonthValue() + 1) + "-" + ldate.getYear();
            } else {
                Date d = us.getFechaNacimiento();
                Calendar c = new GregorianCalendar();
                c.setTime(d);
                strFechaN = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
            }
            this.edtFechaNacimientoA.setText(strFechaN);
            int indexDistrito = displayAllDistritos.indexOf(us.getDistrito().getDistrito());
            this.drop_distritoA.setSelection(indexDistrito + 1);
            this.drop_generoA.setSelection(us.getSexo().equals("H") ? 1 : 2);
            this.edtReferenciaDomicilioReal.setText(us.getDireccion());
            this.edtCeluarA.setText(us.getTelefono());

        } else {
            Toast.makeText(this, "No se pudo recuperar los datos de la seccion actual 😟,por favor reinice la aplicación y vuelva a intentarlo en unos minutos ⚙⚙⚙", Toast.LENGTH_LONG).show();
        }

    }

    private void clearCamposAgraviado() {
        //this.mismaPersona.setChecked(false);
        drop_tipoIdentificacionA.setSelection(0);
        edtDocA.setText("");
        edtNombresA.setText("");
        edtApellidoPaternoA.setText("");
        edtApellidoMaternoA.setText("");
        edtFechaNacimientoA.setText("");
        drop_distritoA.setSelection(0);
        drop_generoA.setSelection(0);
        edtCeluarA.setText("");
        edtReferenciaDomicilioReal.setText("");

    }

    private void guardarAgraviado() {
        Agraviado a;
        if (!edtDocA.getText().toString().equals("") && !edtNombresA.getText().toString().equals("") && !edtApellidoPaternoA.getText().toString().equals("") && !edtApellidoMaternoA.getText().toString().equals("")
                && drop_tipoIdentificacionA.getSelectedItemPosition() != 0
                && !edtFechaNacimientoA.getText().toString().equals("") && !edtCeluarA.getText().toString().equals("")
                && !edtReferenciaDomicilioReal.getText().toString().equals("") && !edtHechoDenunciar.getText().toString().equals("") && drop_medidaProteccion.getSelectedItemPosition() != -1) {
            a = new Agraviado();
            try {
                a.setNumeroIdentificacion(edtDocA.getText().toString());
                a.setNombres(edtNombresA.getText().toString());
                a.setApellidoPaterno(edtApellidoPaternoA.getText().toString());
                a.setApellidoMaterno(edtApellidoMaternoA.getText().toString());
                a.setFechaNacimiento(new SimpleDateFormat("dd-MM-yyyy").parse(edtFechaNacimientoA.getText().toString()));
                a.setTelefono(edtCeluarA.getText().toString());
                a.setDireccion(edtReferenciaDomicilioReal.getText().toString());
                a.setRhd(edtHechoDenunciar.getText().toString());
                int indexDistritoSelected = drop_distritoA.getSelectedItemPosition();
                a.setDistrito(distritos.get(indexDistritoSelected - 1));
                a.setSexo(drop_generoA.getSelectedItem().toString());
                a.setTipoIdentificacion(new TipoIdentificacion());
                final int indexti = drop_tipoIdentificacionA.getSelectedItemPosition();
                a.getTipoIdentificacion().setId(indexti);
                a.setEstadoCivil(new EstadoCivil());
                a.getEstadoCivil().setId(2);
                int indexIA = drop_infoAdicionalA.getSelectedItemPosition();
                a.setInformacionAdicional(infosAdicional.get(indexIA - 1));
                if (drop_medidaProteccion.getSelectedItemPosition() == 1) {
                    a.setMedidaProteccion(true);
                    a.setJuzgado(drop_Juzgado.getSelectedItem().toString());
                    a.setFechaEmision(new SimpleDateFormat("dd-MM-yyyy").parse(edtFechaEmisionProteccion.getText().toString()));
                    a.setDetalleProteccion(edtDetalleProtección.getText().toString());
                }
                Toast.makeText(this, DenunciaManager.addAgraviado(a, this), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error al intentar crear el objeto Agraviado:" + e.getMessage() + " 😥", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            this.clearCamposAgraviado();
        } else {
            Toast.makeText(this, "Por favor complete todos los campos 😑", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarDenunciado() {
        Denunciado d;
        if (!edt_DocD.getText().toString().equals("")
                && !edt_NombresD.getText().toString().equals("")
                && !edt_ApellidoPaternoD.getText().toString().equals("")
                && !edt_ApellidoMaternoD.getText().toString().equals("")
                && !edt_CelularD.getText().toString().equals("")
                && !dtp_FechaNacD.getText().toString().equals("")) {
            d = new Denunciado();
            try {
                d.setTipoIdentificacion(new TipoIdentificacion());
                d.getTipoIdentificacion().setId(sp_TipoIdentificacionD.getSelectedItemPosition() - 1);
                d.getTipoIdentificacion().setId(1);
                d.setNumeroIdentificacion(edt_DocD.getText().toString());
                d.setNombres(edt_NombresD.getText().toString());
                d.setApellidoPaterno(edt_ApellidoPaternoD.getText().toString());
                d.setApellidoMaterno(edt_ApellidoMaternoD.getText().toString());
                d.setTelefono(edt_CelularD.getText().toString());
                int indexIA = sp_InfoAdicionalD.getSelectedItemPosition() - 1;
                d.setInformacionAdicional(infosAdicional.get(indexIA));
                d.setFechaNacimiento(new SimpleDateFormat("dd-MM-yyyy").parse(dtp_FechaNacD.getText().toString()));
                d.setDistrito(distritos.get(sp_DistritoDenunciado.getSelectedItemPosition() - 1));
                d.setDireccion(edt_DireccionD.getText().toString());
                d.setSexo(sp_GeneroD.getSelectedItem().toString());
                d.setEstadoCivil(new EstadoCivil());
                d.getEstadoCivil().setId(2);

                Toast.makeText(this, DenunciaManager.addDenunciado(d, this), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error al intentar crear el objeto Denunciado:" + e.getMessage() + " 😥", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Por favor complete todos los campos 😑", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        //AGRAVIADO
        edtDocA = findViewById(R.id.edtNumeroIdentificacionA);
        edtNombresA = findViewById(R.id.edt_nombresA);
        edtApellidoPaternoA = findViewById(R.id.edt_apellidoPaternoA);
        edtApellidoMaternoA = findViewById(R.id.edt_ApellidoMaternoA);
        edtFechaNacimientoA = findViewById(R.id.edt_FechaNacA);
        edtFechaNacimientoA.setOnClickListener(v ->
                showDatePickerDialog((view, year, month, dayOfMonth) -> {
                    final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                    edtFechaNacimientoA.setText(selectedDate);
                })
        );
        drop_distritoA = findViewById(R.id.dropdown_distritoA);
        drop_generoA = findViewById(R.id.dropdown_generoA);
        edtCeluarA = findViewById(R.id.edt_celularA);
        edtReferenciaDomicilioReal = findViewById(R.id.edt_DireccionA);
        drop_infoAdicionalA = findViewById(R.id.sp_infAdicionalA);
        drop_medidaProteccion = findViewById(R.id.dropdown_medida_proteccion_A);

        drop_Juzgado = findViewById(R.id.dropdown_juzgadoA);
        edtFechaEmisionProteccion = findViewById(R.id.edtFechaEmisionP);
        edtDetalleProtección = findViewById(R.id.edtdetalleproteccion_juez);
        edtHechoDenunciar = findViewById(R.id.edtrelateHechosDenuncia);
        //DENUNCIADO
        edt_DocD = findViewById(R.id.edtNumeroIdentificacionD);
        edt_NombresD = findViewById(R.id.edtnombreD);
        edt_ApellidoPaternoD = findViewById(R.id.edt_ApellidoPaternoD);
        edt_ApellidoMaternoD = findViewById(R.id.edt_ApellidoMaternoD);
        dtp_FechaNacD = findViewById(R.id.dtp_FechaNacD);
        dtp_FechaNacD.setOnClickListener(v -> showDatePickerDialog((view, year, month, dayOfMonth) -> {
            final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            dtp_FechaNacD.setText(selectedDate);
        }));
        edt_CelularD = findViewById(R.id.edt_CelularD);
        edt_DireccionD = findViewById(R.id.edtDireccionD);
        sp_TipoIdentificacionD = findViewById(R.id.sp_tipoIdentificacionD);
        sp_DistritoDenunciado = findViewById(R.id.sp_distritoD);
        sp_EstadoCivilD = findViewById(R.id.sp_estadocivilD);
        sp_InfoAdicionalD = findViewById(R.id.sp_infAdicionalD);
        sp_GeneroD = findViewById(R.id.sp_generoD);


        //BOTONES
        btnSaveA = findViewById(R.id.btnSaveA);
        btnSaveA.setOnClickListener(v -> guardarAgraviado());
        btnSaveD = findViewById(R.id.btnSaveDenunciado);
        btnSaveD.setOnClickListener(v -> guardarDenunciado());
        //DENUNCIA

        edtLugarHechos = findViewById(R.id.edtDireccionHechos);
        edtReferenciaHechos = findViewById(R.id.edtreferenciahechos);

        this.edtFechaHechos = findViewById(R.id.dtpFechaHechos);
        edtFechaHechos.setOnClickListener(v -> showDatePickerDialog((view, year, month, dayOfMonth) -> {
            final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            edtFechaHechos.setText(selectedDate);
        }));
        this.edtFechaEmisionProteccion.setOnClickListener(v -> showDatePickerDialog((view, year, month, dayOfMonth) -> {
            final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            edtFechaEmisionProteccion.setText(selectedDate);
        }));
        drop_distritoD = findViewById(R.id.sp_distritoDenuncia);
        drop_vpd = findViewById(R.id.sp_vinculoParteDenunciada);
        drop_td = findViewById(R.id.sp_tipodenuncia);
        drop_tipoIdentificacionA = findViewById(R.id.sp_tipoIdentificacionA);
        mismaPersona = findViewById(R.id.chkSoyLaMismaPesona);
        mismaPersona.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cambiarEstadoCampos(!isChecked);
        });
        drop_medidaProteccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                asignarCamposProteccion(position == 0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initAdapters() {
        adapterDistritos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayDistritos);
        adapterDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_distritoD.setAdapter(adapterDistritos);

        adapterVinculos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayVinculos);
        adapterVinculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_vpd.setAdapter(adapterVinculos);

        adapterTd = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayTd);
        adapterTd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_td.setAdapter(adapterTd);

        //
        drop_tipoIdentificacionA.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));
        sp_TipoIdentificacionD.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));

        adapterAllDistritos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayAllDistritos);
        adapterAllDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_distritoA.setAdapter(adapterAllDistritos);
        sp_DistritoDenunciado.setAdapter(adapterAllDistritos);

        //
        drop_generoA.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Masculino",
                "Femenino"
        }));
        sp_GeneroD.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Masculino",
                "Femenino"
        }));
        adapterInfAdic = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayInfAdic);
        adapterInfAdic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_infoAdicionalA.setAdapter(adapterInfAdic);
        sp_InfoAdicionalD.setAdapter(adapterInfAdic);

        adapterEstadosCiviles = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayEstadosCiviles);
        sp_EstadoCivilD.setAdapter(adapterEstadosCiviles);

        drop_medidaProteccion.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Sí",
                "No"
        }));
        drop_Juzgado.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Juzgado de Familia"
        }));
    }

    private void loadData() {

        this.distritoViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.distritos.clear();
                this.distritos.addAll(response.getBody());
                displayDistritos.clear();
                for (Distrito d : distritos) {
                    displayDistritos.add(d.getDistrito());
                }
                adapterDistritos.notifyDataSetChanged();
            }
        });
        this.distritoViewModel.listAll().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.allDistritos.clear();
                this.allDistritos.addAll(response.getBody());
                displayAllDistritos.clear();
                for (Distrito d : allDistritos) {
                    displayAllDistritos.add(d.getDistrito());
                }
                adapterAllDistritos.notifyDataSetChanged();
            }
        });

        this.vpdViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.vinculos.clear();
                this.vinculos.addAll(response.getBody());
                displayVinculos.clear();
                for (VinculoParteDenunciada v : vinculos) {
                    displayVinculos.add(v.getNombre());
                }
                adapterVinculos.notifyDataSetChanged();
            }
        });
        this.tdViewModel.listActivos().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.tiposDenuncia.clear();
                this.tiposDenuncia.addAll(response.getBody());
                displayTd.clear();
                for (TipoDenuncia t : tiposDenuncia) {
                    displayTd.add(t.getTipoDenuncia());
                }
                adapterTd.notifyDataSetChanged();
            }
        });
        this.infoAdicViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.infosAdicional.clear();
                this.infosAdicional.addAll(response.getBody());
                this.displayInfAdic.clear();
                for (InformacionAdicional i : this.infosAdicional) {
                    this.displayInfAdic.add(i.getNombre());
                }
                this.adapterInfAdic.notifyDataSetChanged();
            }
        });
        this.ecViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.estadosCiviles.clear();
                this.estadosCiviles.addAll(response.getBody());
                this.displayEstadosCiviles.clear();
                for (EstadoCivil e : this.estadosCiviles) {
                    this.displayEstadosCiviles.add(e.getEstadoCivil());
                }
                this.adapterEstadosCiviles.notifyDataSetChanged();
            }
        });
        this.drop_tipoIdentificacionA.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));
        this.drop_medidaProteccion.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Si",
                "No"
        }));
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(listener);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void save() {
        Denuncia d = new Denuncia();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
            Gson g = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .create();
            d.setUsuario(g.fromJson(preferences.getString("UsuarioJson", null), Usuario.class));
            d.setCod_denuncia("? ? ?");
            d.setPolicia(new Policia());
            d.getPolicia().setId(1);
            d.setFechaDenuncia(new Date());
            d.setFechaHechos(new SimpleDateFormat("dd-MM-yyyy").parse(this.edtFechaHechos.getText().toString()));
            d.setDistrito(this.distritos.get(this.drop_distritoD.getSelectedItemPosition() - 1));
            d.setDireccion(this.edtLugarHechos.getText().toString());
            d.setReferenciaDireccion(this.edtReferenciaHechos.getText().toString());
            d.setVinculoParteDenunciada(this.vinculos.get(this.drop_vpd.getSelectedItemPosition() - 1));
            d.setTipoDenuncia(this.tiposDenuncia.get(this.drop_td.getSelectedItemPosition() - 1));
            DenunciaManager.setDenuncia(d, this);
            Toast.makeText(this, "Los datos de la denuncia fueron guardados correctamente", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "se ha producido un error al intentar armar la denuncia:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
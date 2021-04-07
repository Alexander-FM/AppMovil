package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.comisariaapp.DatePickerFragment;
import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.DenunciaManager;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.InformacionAdicional;
import com.example.comisariaapp.entity.service.TipoDenuncia;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;
import com.example.comisariaapp.viewmodel.DistritoViewModel;
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
    //CON RESPECTO A LOS DATOS DE LA DENUNCIA
    private EditText edtFechaHechos, edtLugarHechos, edtReferenciaHechos;
    private MaterialSpinner drop_distrito, drop_vpd, drop_td;
    //CON RESPECTO A LOS DATOS DEL USUARIO O AGRAVIADO
    private CheckBox mismaPersona;
    public MaterialSpinner drop_tipoIdentificacion, drop_generoA, drop_generoD, drop_distritoA, drop_infoAdicionalA, drop_infoAdicionalD, drop_medidaProteccion, drop_Juzgado;
    private EditText edtDoc, edtApellidos, edtNombres, edtFechaNacimiento, edtFechaEmisionProteccion, edtDistritoAgraviado, edtCeluarA, edtCelularD, edtReferenciaDomicilioReal, edtHechoDenunciar, edtDetalleProtección;
    private Button btnSaveA, btnSaveD;
    private RadioButton rbMasculino, rbFemenino;

    private List<Distrito> distritos = new ArrayList<>(), allDistritos = new ArrayList<>();
    private List<VinculoParteDenunciada> vinculos = new ArrayList<>();
    private List<TipoDenuncia> tiposDenuncia = new ArrayList<>();
    private List<InformacionAdicional> infosAdicional = new ArrayList<>();
    private ArrayAdapter<String> adapterDistritos, adapterAllDistritos, adapterVinculos, adapterTd, adapterInfAdic;
    private List<String> displayDistritos = new ArrayList<>(), displayAllDistritos = new ArrayList<>(), displayVinculos = new ArrayList<>(), displayTd = new ArrayList<>(), displayInfAdic = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_registrar_denuncia);

        ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.vpdViewModel = vmp.get(VinculoParteDenunciadaViewModel.class);
        this.tdViewModel = vmp.get(TipoDenunciaViewModel.class);
        this.infoAdicViewModel = vmp.get(InformacionAdicionalViewModel.class);
        this.init();
        this.initAdapters();
        this.loadData();
        Toolbar toolbar = findViewById(R.id.toolbar);
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
                save();
                startActivity(new Intent(this, PersonasCarritoActity.class));
                overridePendingTransition(R.anim.above_in, R.anim.above_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cambiarEstadoCampos(boolean b) {
        drop_tipoIdentificacion.setEnabled(b);
        edtDoc.setEnabled(b);
        edtNombres.setEnabled(b);
        edtApellidos.setEnabled(b);
        edtFechaNacimiento.setEnabled(b);
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
        //edtCeluarA.setEnabled(b);
        edtReferenciaDomicilioReal.setEnabled(b);
        drop_infoAdicionalA.setEnabled(b);
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
            this.drop_tipoIdentificacion.setSelection(us.getTipoIdentificacion().getId() == 1 ? 0 : 1);
            this.edtDoc.setText(us.getNumeroIdentificacion());
            this.edtNombres.setText(us.getNombres());
            this.edtApellidos.setText(us.getApellidoPaterno() + " " + us.getApellidoMaterno());
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
            this.edtFechaNacimiento.setText(strFechaN);
            int indexDistrito = displayAllDistritos.indexOf(us.getDistrito().getDistrito());
            this.drop_distritoA.setSelection(indexDistrito);
            this.drop_generoA.setSelection(us.getSexo().equals("M") ? 0 : 1);
            this.edtReferenciaDomicilioReal.setText(us.getDireccion());

        } else {
            Toast.makeText(this, "No se pudo recuperar los datos de la seccion actual 😟,por favor reinice la aplicación y vuelva a intentarlo en unos minutos ⚙⚙⚙", Toast.LENGTH_LONG).show();
        }

    }

    private void clearCamposAgraviado() {
        drop_tipoIdentificacion.setSelection(0);
        edtDoc.setText("");
        edtNombres.setText("");
        edtApellidos.setText("");
        edtFechaNacimiento.setText("");
        drop_distritoA.setSelection(0);
        drop_generoA.setSelection(0);
        edtCeluarA.setText("");
        edtReferenciaDomicilioReal.setText("");
    }

    private void guardarAgraviado() {
        Agraviado a;
        if (edtDoc.getText().toString() != "" && edtNombres.getText().toString() != "" && edtApellidos.getText().toString() != ""
                && edtFechaNacimiento.getText().toString() != "" && edtCeluarA.getText().toString() != ""
                && edtReferenciaDomicilioReal.getText().toString() != "" && edtHechoDenunciar.getText().toString() != "") {
            a = new Agraviado();
            try {
                a.setNumeroDoc(edtDoc.getText().toString());
                a.setNombreAgraviado(edtNombres.getText().toString());
                a.setApellidosAgraviado(edtApellidos.getText().toString());
                a.setFechaNac(new SimpleDateFormat("dd-MM-yyyy").parse(edtFechaNacimiento.getText().toString()));
                a.setTelefono(edtCeluarA.getText().toString());
                a.setDireccionAgraviado(edtReferenciaDomicilioReal.getText().toString());
                a.setRHD(edtHechoDenunciar.getText().toString());
                int indexDistritoSelected = drop_distritoA.getSelectedItemPosition();
                a.setDistrito(distritos.get(indexDistritoSelected));
                a.setSexo(drop_generoA.getSelectedItem().toString());
                if (drop_medidaProteccion.getSelectedItemPosition() == 1) {
                    a.setMedidaProteccion(true);
                    a.setJuzgado(drop_Juzgado.getSelectedItem().toString());
                    a.setFechaEmision(new SimpleDateFormat("dd-MM-yyyy").parse(edtFechaEmisionProteccion.getText().toString()));
                    a.setDetalleProteccion(edtDetalleProtección.getText().toString());
                }
                DenunciaManager.addAgraviado(a);
            } catch (Exception e) {
                Toast.makeText(this, "Error al intentar crear el objeto Agraviado:" + e.getMessage() + " 😥", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Por favor complete todos los campos 😑", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {

        edtDoc = findViewById(R.id.edtNumeroIdentificacion);
        edtNombres = findViewById(R.id.edtnombreA);
        edtApellidos = findViewById(R.id.edtapellidosA);
        edtFechaNacimiento = findViewById(R.id.edtFechaNacA);
        edtFechaNacimiento.setOnClickListener(v -> {
            showDatePickerDialog((view, year, month, dayOfMonth) -> {
                final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                edtFechaNacimiento.setText(selectedDate);
            });
        });
        drop_distritoA = findViewById(R.id.dropdown_distritoA);
        drop_generoA = findViewById(R.id.dropdown_generoA);
        edtCeluarA = findViewById(R.id.edtcelularA);
        edtReferenciaDomicilioReal = findViewById(R.id.edtDireccionA);
        drop_infoAdicionalA = findViewById(R.id.dropdown_infAdicionalA);
        drop_medidaProteccion = findViewById(R.id.dropdown_medida_proteccion_A);

        drop_Juzgado = findViewById(R.id.dropdown_juzgadoA);
        edtFechaEmisionProteccion = findViewById(R.id.edtFechaEmisionP);
        edtDetalleProtección = findViewById(R.id.edtdetalleproteccion_juez);
        edtHechoDenunciar = findViewById(R.id.edtrelateHechosDenuncia);

        btnSaveA = findViewById(R.id.btnSaveA);
        btnSaveA.setOnClickListener(v -> guardarAgraviado());
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
        drop_distrito = findViewById(R.id.dropdown_distrito);
        drop_vpd = findViewById(R.id.dropdown_vinculoParteDenunciada);
        drop_td = findViewById(R.id.dropdown_tipodenuncia);
        drop_tipoIdentificacion = findViewById(R.id.dropdown_tipoIdentificacion);
        mismaPersona = findViewById(R.id.chkSoyLaMismaPesona);
        mismaPersona.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cambiarEstadoCampos(!isChecked);
        });
    }

    private void initAdapters() {
        adapterDistritos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayDistritos);
        adapterDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_distrito.setAdapter(adapterDistritos);

        adapterVinculos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayVinculos);
        adapterVinculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_vpd.setAdapter(adapterVinculos);

        adapterTd = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayTd);
        adapterTd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_td.setAdapter(adapterTd);

        //
        drop_tipoIdentificacion.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));
        adapterAllDistritos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayAllDistritos);
        adapterAllDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_distritoA.setAdapter(adapterAllDistritos);

        //
        drop_generoA.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Masculino",
                "Femenino"
        }));

        adapterInfAdic = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayInfAdic);
        adapterInfAdic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_infoAdicionalA.setAdapter(adapterInfAdic);

        drop_medidaProteccion.setAdapter(new ArrayAdapter<>(this,  android.R.layout.simple_spinner_dropdown_item, new String[]{
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
                displayInfAdic.clear();
                for (InformacionAdicional i : infosAdicional) {
                    displayInfAdic.add(i.getNombre());
                }
                adapterInfAdic.notifyDataSetChanged();
            }
        });
        drop_tipoIdentificacion.setAdapter(new ArrayAdapter<>(this,
                R.layout.dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));
        drop_medidaProteccion.setAdapter(new ArrayAdapter<>(this, R.layout.dropdown_item, new String[]{
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
            d.setFechaHechos(new SimpleDateFormat("dd-MM-yyyy").parse(this.edtFechaHechos.getText().toString()));
            d.setDistrito(this.distritos.get(this.drop_distrito.getSelectedItemPosition()));
            d.setDireccion(this.edtLugarHechos.getText().toString());
            d.setReferenciaDireccion(this.edtReferenciaHechos.getText().toString());
            d.setVinculoParteDenunciada(this.vinculos.get(this.drop_vpd.getSelectedItemPosition()));
            d.setTipoDenuncia(this.tiposDenuncia.get(this.drop_td.getSelectedItemPosition()));
            DenunciaManager.set(d);
        } catch (Exception e) {
            Toast.makeText(this, "se ha producido un error al intentar armar la denuncia:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
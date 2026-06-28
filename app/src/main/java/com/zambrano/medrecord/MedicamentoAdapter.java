package com.zambrano.medrecord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

// El Adapter es el puente entre la lista de datos (List<Medicamento>)
// y las vistas del RecyclerView. Reutiliza las vistas con el patron ViewHolder.
public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder> {

    private List<Medicamento> lista = new ArrayList<>();
    private final OnItemClickListener listener;

    // Interfaz para comunicar eventos de click al Activity
    public interface OnItemClickListener {
        void onEdit(Medicamento medicamento);
        void onDelete(Medicamento medicamento);
    }

    public MedicamentoAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder: guarda referencias a las vistas de cada item para no
    // llamar a findViewById cada vez que se recicla una vista.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDosis, tvUnidad;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreMed);
            tvDosis = itemView.findViewById(R.id.tvDosisMed);
            tvUnidad = itemView.findViewById(R.id.tvUnidadMed);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de cada item de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicamento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicamento med = lista.get(position);
        holder.tvNombre.setText(med.getNombre());
        holder.tvDosis.setText(String.valueOf(med.getDosisMg()));
        holder.tvUnidad.setText(med.getUnidad());

        // Click simple -> editar
        holder.itemView.setOnClickListener(v -> listener.onEdit(med));

        // Click largo -> eliminar con confirmacion
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDelete(med);
            return true;
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    // Actualiza la lista y refresca el RecyclerView
    public void setLista(List<Medicamento> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }
}

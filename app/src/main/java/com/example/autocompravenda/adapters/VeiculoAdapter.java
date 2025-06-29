package com.example.autocompravenda.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocompravenda.R;
import com.example.autocompravenda.activities.DetalheVeiculoActivity;
import com.example.autocompravenda.models.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class VeiculoAdapter extends RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder> {

    private List<Veiculo> veiculos;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Veiculo veiculo);
    }

    public VeiculoAdapter(List<Veiculo> veiculos, Context context, OnItemClickListener listener) {
        this.veiculos = veiculos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VeiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_veiculo, parent, false);
        return new VeiculoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeiculoViewHolder holder, int position) {
        Veiculo veiculo = veiculos.get(position);
        holder.tvMarcaModelo.setText(veiculo.marca + " " + veiculo.modelo);
        holder.tvAno.setText("Ano: " + veiculo.ano);
        holder.tvPreco.setText(String.format("Preço: R$ %.2f", veiculo.preco));

        if (veiculo.imagemUri != null && !veiculo.imagemUri.isEmpty()) {
            holder.ivItemFoto.setImageURI(Uri.parse(veiculo.imagemUri));
        } else {
            holder.ivItemFoto.setImageResource(R.drawable.ic_auto_image); // imagem default
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalheVeiculoActivity.class);
            intent.putExtra("veiculo_id", veiculo.id);
            context.startActivity(intent);
        });
        holder.itemView.setOnClickListener((view -> listener.onItemClick(veiculo)));
    }

    @Override
    public int getItemCount() {
        return veiculos != null ? veiculos.size() : 0;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public static class VeiculoViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarcaModelo, tvAno, tvPreco;
        ImageView ivItemFoto;

        public VeiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarcaModelo = itemView.findViewById(R.id.tvMarcaModelo);
            tvAno = itemView.findViewById(R.id.tvAno);
            tvPreco = itemView.findViewById(R.id.tvPreco);
            ivItemFoto = itemView.findViewById(R.id.ivItemFoto);
        }
    }
}

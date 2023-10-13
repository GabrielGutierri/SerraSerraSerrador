package br.com.serra_serra_serrador.serra_serra_serrador.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GraphResponseModel<T>{
    private String status;
    private T data;
}

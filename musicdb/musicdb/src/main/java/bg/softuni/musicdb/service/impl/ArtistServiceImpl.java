package bg.softuni.musicdb.service.impl;

import bg.softuni.musicdb.model.entities.ArtistEntity;
import bg.softuni.musicdb.repositories.ArtistRepository;
import bg.softuni.musicdb.service.ArtistService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final Gson gson;
    private final ArtistRepository artistRepository;
    private final Resource artistFile;

    public ArtistServiceImpl(@Value("classpath:init/artist.json") Resource artistFile,
        Gson gson,
        ArtistRepository artistRepository){

        this.gson = gson;
        this.artistRepository = artistRepository;
        this.artistFile = artistFile;
    }

    @Override
    public void seedArtist() {
        if(artistRepository.count() == 0){
            try {
                ArtistEntity[] artistEntities = gson.fromJson(Files.readString(Path.of(artistFile.getURI())), ArtistEntity[].class);

                Arrays.stream(artistEntities).forEach(artistRepository::save);
            } catch (IOException e) {
                throw  new IllegalStateException("Cannot seed artists.....");
            }
        }
    }
}

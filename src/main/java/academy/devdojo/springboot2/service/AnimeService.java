package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.domain.dto.AnimeDto;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Page<AnimeDto> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable).map(AnimeMapper.INSTANCE::toAnimeDto);
    }

    public List<AnimeDto> listAllNonPageable() {
        return animeRepository.findAll().stream().map(AnimeMapper.INSTANCE::toAnimeDto)
                .collect(Collectors.toList());
    }

    public List<AnimeDto> findByName(String name) {
        return animeRepository.findByName(name).stream().map(AnimeMapper.INSTANCE::toAnimeDto)
                .collect(Collectors.toList());
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not Found"));
    }

    public AnimeDto findById(long id) {
        Anime anime = findByIdOrThrowBadRequestException(id);
        return AnimeMapper.INSTANCE.toAnimeDto(anime);
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);
    }
}
